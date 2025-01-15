package com.zhikaixu.serviceorder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zhikaixu.internalcommon.constant.CommonStatusEnum;
import com.zhikaixu.internalcommon.constant.DriverCarConstants;
import com.zhikaixu.internalcommon.constant.IdentityConstant;
import com.zhikaixu.internalcommon.constant.OrderConstants;
import com.zhikaixu.internalcommon.dto.Car;
import com.zhikaixu.internalcommon.dto.OrderInfo;
import com.zhikaixu.internalcommon.dto.PriceRule;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.DriverGrabRequest;
import com.zhikaixu.internalcommon.request.OrderRequest;
import com.zhikaixu.internalcommon.response.OrderDriverResponse;
import com.zhikaixu.internalcommon.response.TerminalResponse;
import com.zhikaixu.internalcommon.response.TrsearchResponse;
import com.zhikaixu.internalcommon.util.RedisPrefixUtils;
import com.zhikaixu.serviceorder.mapper.OrderInfoMapper;
import com.zhikaixu.serviceorder.remote.ServiceDriverUserClient;
import com.zhikaixu.serviceorder.remote.ServiceMapClient;
import com.zhikaixu.serviceorder.remote.ServicePriceClient;
import com.zhikaixu.serviceorder.remote.ServiceSsePushClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhikaixu
 * @since 2024-12-30
 */
@Slf4j
@Service
public class OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ServicePriceClient servicePriceClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    ServiceDriverUserClient serviceDriverUserClient;

    /**
     * 新建订单
     * @param orderRequest
     * @return
     */
    public ResponseResult add(OrderRequest orderRequest) {
        // 判断城市是否有司机
        ResponseResult<Boolean> driverAvailable = serviceDriverUserClient.isDriverAvailable(orderRequest.getAddress());
        log.info("判断城市是否有司机结果: " + driverAvailable.getData());
        if (!driverAvailable.getData()) {
            return ResponseResult.fail(CommonStatusEnum.CITY_DRIVER_EMPTY.getCode(), CommonStatusEnum.CITY_DRIVER_EMPTY.getValue());
        }

        // 需要判断计价规则的版本是否为最新
        ResponseResult<Boolean> isNew = servicePriceClient.isNew(orderRequest.getFareType(), orderRequest.getFareVersion());
        if (!(isNew.getData())) {
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_CHANGED.getCode(), CommonStatusEnum.PRICE_RULE_CHANGED.getValue());
        }

        // 设置key，校验原来有没有key
        if (isBlackDevice(orderRequest)) {
            return ResponseResult.fail(CommonStatusEnum.DEVICE_IS_BLACK.getCode(), CommonStatusEnum.DEVICE_IS_BLACK.getValue());
        }

        // 判断下单的城市和计价规则是否正常
        if (!isPriceRuleExists(orderRequest)) {
            return ResponseResult.fail(CommonStatusEnum.CITY_SERVICE_NOT_SERVE.getCode(), CommonStatusEnum.CITY_SERVICE_NOT_SERVE.getValue());
        }

        // 判断有正在进行的订单，不允许下单
        if (isPassengerOrderGoingon(orderRequest.getPassengerId()) > 0) {
            return ResponseResult.fail(CommonStatusEnum.ORDER_GOING_ON.getCode(), CommonStatusEnum.ORDER_GOING_ON.getValue());
        }
        // 创建订单
        OrderInfo orderInfo = new OrderInfo();

        BeanUtils.copyProperties(orderRequest, orderInfo);

        orderInfo.setOrderStatus(OrderConstants.ORDER_START);

        LocalDateTime now = LocalDateTime.now();
        orderInfo.setGmtCreate(now);
        orderInfo.setGmtModified(now);

        orderInfoMapper.insert(orderInfo);

        // 定时任务处理
        for (int i = 0; i < 6; i++) {
            // 派单
            int result = dispatchRealTimeOrder(orderInfo);
            if (result == 1) {
                break;
            }
            if (i == 5) {
                // 订单无效
                orderInfo.setOrderStatus(OrderConstants.ORDER_INVALID);
                orderInfoMapper.updateById(orderInfo);
            } else {
                // 等待20秒
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        return ResponseResult.success("");
    }

    /**
     * 新建订单
     * @param orderRequest
     * @return
     */
    public ResponseResult book(OrderRequest orderRequest) {
//        // 判断城市是否有司机
//        ResponseResult<Boolean> driverAvailable = serviceDriverUserClient.isDriverAvailable(orderRequest.getAddress());
//        log.info("判断城市是否有司机结果: " + driverAvailable.getData());
//        if (!driverAvailable.getData()) {
//            return ResponseResult.fail(CommonStatusEnum.CITY_DRIVER_EMPTY.getCode(), CommonStatusEnum.CITY_DRIVER_EMPTY.getValue());
//        }

//        // 需要判断计价规则的版本是否为最新
//        ResponseResult<Boolean> isNew = servicePriceClient.isNew(orderRequest.getFareType(), orderRequest.getFareVersion());
//        if (!(isNew.getData())) {
//            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_CHANGED.getCode(), CommonStatusEnum.PRICE_RULE_CHANGED.getValue());
//        }

//        // 设置key，校验原来有没有key
//        if (isBlackDevice(orderRequest)) {
//            return ResponseResult.fail(CommonStatusEnum.DEVICE_IS_BLACK.getCode(), CommonStatusEnum.DEVICE_IS_BLACK.getValue());
//        }
//
//        // 判断下单的城市和计价规则是否正常
//        if (!isPriceRuleExists(orderRequest)) {
//            return ResponseResult.fail(CommonStatusEnum.CITY_SERVICE_NOT_SERVE.getCode(), CommonStatusEnum.CITY_SERVICE_NOT_SERVE.getValue());
//        }

//        // 判断有正在进行的订单，不允许下单
//        if (isPassengerOrderGoingon(orderRequest.getPassengerId()) > 0) {
//            return ResponseResult.fail(CommonStatusEnum.ORDER_GOING_ON.getCode(), CommonStatusEnum.ORDER_GOING_ON.getValue());
//        }
        // 创建订单
        OrderInfo orderInfo = new OrderInfo();

        BeanUtils.copyProperties(orderRequest, orderInfo);

        orderInfo.setOrderStatus(OrderConstants.ORDER_START);

        LocalDateTime now = LocalDateTime.now();
        orderInfo.setGmtCreate(now);
        orderInfo.setGmtModified(now);

        orderInfoMapper.insert(orderInfo);

        // 定时任务处理
        for (int i = 0; i < 6; i++) {
            // 派单
            int result = dispatchBookOrder(orderInfo);
            if (result == 1) {
                break;
            }

            // 等待20秒
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
        return ResponseResult.success("");
    }

    /**
     * 判断乘客是否有 正在进行的订单
     * @param passengerId
     * @return
     */
    private int isPassengerOrderGoingon(Long passengerId) {
        // 判断有正在进行的订单，不允许下单
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("passenger_id", passengerId);
        queryWrapper.and(wrapper->wrapper.eq("order_status", OrderConstants.ORDER_START)
                .or().eq("order_status", OrderConstants.DRIVER_RECEIVE_ORDER)
                .or().eq("order_status", OrderConstants.DRIVER_TO_PICK_UP_PASSENGER)
                .or().eq("order_status", OrderConstants.DRIVER_ARRIVED_DEPARTURE)
                .or().eq("order_status", OrderConstants.PICK_UP_PASSENGER)
                .or().eq("order_status", OrderConstants.PASSENGER_GETOFF)
                .or().eq("order_status", OrderConstants.TO_START_PAY)
        );

        Integer validOrderNumber = orderInfoMapper.selectCount(queryWrapper);
        return validOrderNumber;

    }

    /**
     * 判断下单的设备是否是黑名单设备
     * @param orderRequest
     * @return
     */
    private boolean isBlackDevice(OrderRequest orderRequest) {
        // 判断下单的设备是否是黑名单设备
        String deviceCode = orderRequest.getDeviceCode();
        // 生成key
        String deviceCodeKey = RedisPrefixUtils.blackDeviceCodePredix + deviceCode;
        Boolean b = stringRedisTemplate.hasKey(deviceCodeKey);
        if (b) {
            String s = stringRedisTemplate.opsForValue().get(deviceCodeKey);
            int i = Integer.parseInt(s);
            if (i >= 2) {
                // 当前设备超过下单次数
                return true;
            } else {
                stringRedisTemplate.opsForValue().increment(deviceCodeKey);
            }
        } else {
            stringRedisTemplate.opsForValue().setIfAbsent(deviceCodeKey, "1", 1L, TimeUnit.HOURS);
        }
        return false;
    }

    /**
     * 计价规则是否存在
     * @param orderRequest
     * @return
     */
    private boolean isPriceRuleExists(OrderRequest orderRequest) {
        String fareType = orderRequest.getFareType();
        int index = fareType.indexOf("$");
        String cityCode = fareType.substring(0, index);
        String vehicleType = fareType.substring(index + 1);

        PriceRule priceRule = new PriceRule();
        priceRule.setCityCode(cityCode);
        priceRule.setVehicleType(vehicleType);
        ResponseResult<Boolean> booleanResponseResult = servicePriceClient.ifPriceExists(priceRule);
        return booleanResponseResult.getData();
    }

    @Autowired
    private ServiceMapClient serviceMapClient;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ServiceSsePushClient serviceSsePushClient;

    /**
     * 实时订单排单逻辑
     * @param orderInfo
     * @return 0: 派单失败；1: 派单成功
     */
    public int dispatchRealTimeOrder(OrderInfo orderInfo) {
        log.info("派单循环一次");
        int result = 0;

        // 2km
        String depLatitude = orderInfo.getDepLatitude();
        String depLongitude = orderInfo.getDepLongitude();
        String center = depLatitude + "," + depLongitude;

        List<Integer> radiusList = new ArrayList<>();
        radiusList.add(2000);
        radiusList.add(4000);
        radiusList.add(5000);

        // 搜索结果
        ResponseResult<List<TerminalResponse>> listResponseResult = null;
        for (int i = 0; i < radiusList.size(); i++) {
            Integer radius = radiusList.get(i);
            listResponseResult = serviceMapClient.terminalAroundSearch(center, radius);

            log.info("在半径为" + radius + "寻找车辆");

            // 获得终端 [{"tid":"1129053043","carId":1873418460615217160},{"tid":"1132790395","carId":1873418460615217164}]

            // 解析终端
            List<TerminalResponse> data = listResponseResult.getData();
            for (TerminalResponse terminalResponse : data) {
                Long carId = terminalResponse.getCarId();
                String longitude = terminalResponse.getLongitude();
                String latitude = terminalResponse.getLatitude();

                // 查询是否有对应的可派单司机
                ResponseResult<OrderDriverResponse> availableDriver = serviceDriverUserClient.getAvailableDriver(carId);
                if (availableDriver.getCode() == CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getCode()) {
                    log.info("没有车辆Id：" + carId + "对应的司机");
                    continue;
                } else {
                    log.info("找到了正在出车的司机，carId: " + carId);
                    OrderDriverResponse orderDriverResponse = availableDriver.getData();
                    Long driverId = orderDriverResponse.getDriverId();
                    String driverPhone = orderDriverResponse.getDriverPhone();
                    String licenceId = orderDriverResponse.getLicenseId();
                    String vehicleNo = orderDriverResponse.getVehicleNo();
                    String vehicleTypeFromCar = orderDriverResponse.getVehicleType();
                    // 锁司机id的方式要注意
                    // 原来driverId是线程的局部变量，每个线程都有自己的driverId局部变量，因此会出现数据冲突
                    // 现在将driverId变成字符串并加上intern方法，可以让所有线程都去字符串常量池取这个字符串的引用，如果常量池中没有，先创建，再返回引用

//                    synchronized ((driverId + "").intern()) {

                    // 判断车辆的车型是否符合
                    String vehicleType = orderInfo.getVehicleType();
                    if (!vehicleType.trim().equals(vehicleTypeFromCar.trim())) {
                        System.out.println("车型不符合");
                        continue;
                    }

                    // 判断司机是否有正在进行中的订单
                    String lockKey = (driverId + "").intern();
                    RLock lock = redissonClient.getLock(lockKey);
                    lock.lock();

                    if (isDriverOrderGoingon(driverId) > 0) {
                        lock.unlock();
                        continue;
                    }
                    // 订单直接匹配司机
                    // 查询当前车辆信息
//                    QueryWrapper<Car> carQueryWrapper = new QueryWrapper<>();
//                    carQueryWrapper.eq("id", carId);
                    // 查询当前司机信息
                    orderInfo.setDriverId(driverId);
                    orderInfo.setDriverPhone(driverPhone);
                    orderInfo.setCarId(carId);
                    // 从地图中来
                    orderInfo.setReceiveOrderCarLongitude(longitude);
                    orderInfo.setReceiveOrderCarLatitude(latitude);

                    orderInfo.setReceiveOrderTime(LocalDateTime.now());

                    orderInfo.setLicenseId(licenceId);
                    orderInfo.setVehicleNo(vehicleNo);
                    orderInfo.setOrderStatus(OrderConstants.DRIVER_RECEIVE_ORDER);

                    orderInfoMapper.updateById(orderInfo);

                    // 通知司机
                    JsonObject driverContent = new JsonObject();
                    driverContent.addProperty("orderId", orderInfo.getId());
                    driverContent.addProperty("passengerId", orderInfo.getPassengerId());
                    driverContent.addProperty("passengerPhone", orderInfo.getPassengerPhone());
                    driverContent.addProperty("departure", orderInfo.getDeparture());
                    driverContent.addProperty("depLongitude", orderInfo.getDepLongitude());
                    driverContent.addProperty("depLatitude", orderInfo.getDepLatitude());
                    driverContent.addProperty("destination", orderInfo.getDestination());
                    driverContent.addProperty("destLongitude", orderInfo.getDestLongitude());
                    driverContent.addProperty("destLatitude", orderInfo.getDestLatitude());

                    serviceSsePushClient.push(driverId, IdentityConstant.DRIVER_IDENTITY, driverContent.toString());

                    // 通知乘客
                    JsonObject passengerContent = new JsonObject();
                    passengerContent.addProperty("orderId", orderInfo.getId());
                    passengerContent.addProperty("driverId", orderInfo.getDriverId());
                    passengerContent.addProperty("driverPhone", orderInfo.getDriverPhone());
                    passengerContent.addProperty("vehicleNo", orderInfo.getVehicleNo());
                    // 车辆信息，调用车辆服务查询
                    ResponseResult<Car> carById = serviceDriverUserClient.getCarById(carId);
                    Car carRemote = carById.getData();
                    passengerContent.addProperty("brand", carRemote.getBrand());
                    passengerContent.addProperty("model", carRemote.getModel());
                    passengerContent.addProperty("vehicleColor", carRemote.getVehicleColor());

                    passengerContent.addProperty("receiveOrderCarLongitude", orderInfo.getReceiveOrderCarLongitude());
                    passengerContent.addProperty("receiveOrderCarLatitude", orderInfo.getReceiveOrderCarLatitude());


                    serviceSsePushClient.push(orderInfo.getPassengerId(), IdentityConstant.PASSENGER_IDENTITY, passengerContent.toString());

                    result = 1;
                    lock.unlock();
                    break;
//                    }

                }
            }
        }

        List<TerminalResponse> data = listResponseResult.getData();
        if (data.isEmpty()) {
            log.info("此轮排单没找到车，找了2km, 4km, 5km");
        } else {
            Gson gson = new Gson();
            String json = gson.toJson(data);
            log.info("此轮排单找到了车: {}", json);

        }

        return result;
    }

    /**
     * 预约订单-分发订单
     * @param orderInfo
     * @return 0: 派单失败；1: 派单成功
     */
    public int dispatchBookOrder(OrderInfo orderInfo) {
        log.info("派单循环一次");
        int result = 0;

        // 2km
        String depLatitude = orderInfo.getDepLatitude();
        String depLongitude = orderInfo.getDepLongitude();
        String center = depLatitude + "," + depLongitude;

        List<Integer> radiusList = new ArrayList<>();
        radiusList.add(2000);
        radiusList.add(4000);
        radiusList.add(5000);

        // 搜索结果
        ResponseResult<List<TerminalResponse>> listResponseResult = null;
        radius:
        for (int i = 0; i < radiusList.size(); i++) {
            Integer radius = radiusList.get(i);
            listResponseResult = serviceMapClient.terminalAroundSearch(center, radius);

            log.info("在半径为" + radius + "寻找车辆");

            // 测试：为了测试是否从地图上获取到司机
            TerminalResponse testTerminal = new TerminalResponse();
            testTerminal.setCarId(1873418460615217170L);
            testTerminal.setTid("1149084048");
            testTerminal.setLatitude("39.908145");
            testTerminal.setLongitude("116.411546");

            // 解析终端
            List<TerminalResponse> data = listResponseResult.getData();
            data.add(testTerminal);

            for (TerminalResponse terminalResponse : data) {
                Long carId = terminalResponse.getCarId();
                String longitude = terminalResponse.getLongitude();
                String latitude = terminalResponse.getLatitude();

                // 查询是否有对应的可派单司机
                ResponseResult<OrderDriverResponse> availableDriver = serviceDriverUserClient.getAvailableDriver(carId);
                if (availableDriver.getCode() == CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getCode()) {
                    log.info("没有车辆Id：" + carId + "对应的司机");
                    continue;
                } else {
                    log.info("找到了正在出车的司机，carId: " + carId);
                    OrderDriverResponse orderDriverResponse = availableDriver.getData();
                    Long driverId = orderDriverResponse.getDriverId();
                    String driverPhone = orderDriverResponse.getDriverPhone();
                    String licenceId = orderDriverResponse.getLicenseId();
                    String vehicleNo = orderDriverResponse.getVehicleNo();
                    String vehicleTypeFromCar = orderDriverResponse.getVehicleType();
                    // 锁司机id的方式要注意
                    // 原来driverId是线程的局部变量，每个线程都有自己的driverId局部变量，因此会出现数据冲突
                    // 现在将driverId变成字符串并加上intern方法，可以让所有线程都去字符串常量池取这个字符串的引用，如果常量池中没有，先创建，再返回引用

//                    synchronized ((driverId + "").intern()) {

                    // 判断车辆的车型是否符合
                    String vehicleType = orderInfo.getVehicleType();
                    if (!vehicleType.trim().equals(vehicleTypeFromCar.trim())) {
                        System.out.println("车型不符合");
                        continue;
                    }

                    // 通知司机
                    JsonObject driverContent = new JsonObject();
                    driverContent.addProperty("orderId", orderInfo.getId());
                    driverContent.addProperty("passengerId", orderInfo.getPassengerId());
                    driverContent.addProperty("passengerPhone", orderInfo.getPassengerPhone());
                    driverContent.addProperty("departure", orderInfo.getDeparture());
                    driverContent.addProperty("depLongitude", orderInfo.getDepLongitude());
                    driverContent.addProperty("depLatitude", orderInfo.getDepLatitude());
                    driverContent.addProperty("destination", orderInfo.getDestination());
                    driverContent.addProperty("destLongitude", orderInfo.getDestLongitude());
                    driverContent.addProperty("destLatitude", orderInfo.getDestLatitude());

                    serviceSsePushClient.push(driverId, IdentityConstant.DRIVER_IDENTITY, driverContent.toString());
                    result = 1;
                    break radius;
//                    }

                }
            }
        }

        List<TerminalResponse> data = listResponseResult.getData();
        if (data.isEmpty()) {
            log.info("此轮排单没找到车，找了2km, 4km, 5km");
        } else {
            Gson gson = new Gson();
            String json = gson.toJson(data);
            log.info("此轮排单找到了车: {}", json);

        }

        return result;
    }

    /**
     * 判断司机是否有 正在进行的订单
     * @param driverId
     * @return
     */
    private int isDriverOrderGoingon(Long driverId) {
        // 判断有正在进行的订单，不允许下单
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id", driverId);
        queryWrapper.and(wrapper->wrapper
                .or().eq("order_status", OrderConstants.DRIVER_RECEIVE_ORDER)
                .or().eq("order_status", OrderConstants.DRIVER_TO_PICK_UP_PASSENGER)
                .or().eq("order_status", OrderConstants.DRIVER_ARRIVED_DEPARTURE)
                .or().eq("order_status", OrderConstants.PICK_UP_PASSENGER)
        );

        Integer validOrderNumber = orderInfoMapper.selectCount(queryWrapper);
        log.info("司机id: " + driverId + " 正在进行的订单的数量: " + validOrderNumber);
        return validOrderNumber;

    }

    /**
     * 司机去接乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult toPickUpPassenger(OrderRequest orderRequest) {
        Long orderId = orderRequest.getOrderId();
        LocalDateTime toPickUpPassengerTime = orderRequest.getToPickUpPassengerTime();
        String toPickUpPassengerLongitude = orderRequest.getToPickUpPassengerLongitude();
        String toPickUpPassengerLatitude = orderRequest.getToPickUpPassengerLatitude();
        String toPickUpPassengerAddress = orderRequest.getToPickUpPassengerAddress();
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);

        orderInfo.setToPickUpPassengerTime(toPickUpPassengerTime);
        orderInfo.setToPickUpPassengerLongitude(toPickUpPassengerLongitude);
        orderInfo.setToPickUpPassengerLatitude(toPickUpPassengerLatitude);
        orderInfo.setToPickUpPassengerAddress(toPickUpPassengerAddress);
        orderInfo.setOrderStatus(OrderConstants.DRIVER_TO_PICK_UP_PASSENGER);
        orderInfoMapper.updateById(orderInfo);

        return ResponseResult.success("");
    }

    public ResponseResult arrivedDeparture(OrderRequest orderRequest) {
        Long orderId = orderRequest.getOrderId();

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId);

        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
        orderInfo.setOrderStatus(OrderConstants.DRIVER_ARRIVED_DEPARTURE);

        orderInfo.setDriverArrivedDepartureTime(LocalDateTime.now());
        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success("");

    }

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult pickUpPassenger(OrderRequest orderRequest) {
        Long orderId = orderRequest.getOrderId();

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId);

        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
        orderInfo.setOrderStatus(OrderConstants.PICK_UP_PASSENGER);
        orderInfo.setPickUpPassengerLongitude(orderRequest.getPickUpPassengerLongitude());
        orderInfo.setPickUpPassengerLatitude(orderRequest.getPickUpPassengerLatitude());
        orderInfo.setPickUpPassengerTime(LocalDateTime.now());

        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success("");
    }

    public ResponseResult passengerGetOff(OrderRequest orderRequest) {
        Long orderId = orderRequest.getOrderId();

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId);

        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
        orderInfo.setOrderStatus(OrderConstants.PASSENGER_GETOFF);
        orderInfo.setPassengerGetoffLongitude(orderRequest.getPassengerGetoffLongitude());
        orderInfo.setPassengerGetoffLatitude(orderRequest.getPassengerGetoffLatitude());
        orderInfo.setPassengerGetoffTime(LocalDateTime.now());

        // 订单行驶的路程和时间,调用service-map
        ResponseResult<Car> carById = serviceDriverUserClient.getCarById(orderInfo.getCarId());
        Long starttime = orderInfo.getPickUpPassengerTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println("开始时间: " + starttime + " unix: " + orderInfo.getPickUpPassengerTime());
        Long endtime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
//        Long endtime = orderInfo.getPassengerGetoffTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println("结束时间：" + endtime);
        ResponseResult<TrsearchResponse> trsearch = serviceMapClient.trsearch(carById.getData().getTid(), starttime, endtime);
        TrsearchResponse data = trsearch.getData();
        Long driveMile = data.getDriveMile();
        Long driveTime = data.getDriveTime();
        orderInfo.setDriveMile(driveMile);
        orderInfo.setDriveTime(driveTime);

        // 获取价格
        String address = orderInfo.getAddress();
        String vehicleType = orderInfo.getVehicleType();
        ResponseResult<Double> doubleResponseResult = servicePriceClient.calculatePrice(driveMile.intValue(), driveTime.intValue(), address, vehicleType);
        Double price = doubleResponseResult.getData();
        orderInfo.setPrice(price);

        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success("");
    }

    public ResponseResult pushPayInfo(OrderRequest orderRequest) {

        Long orderId = orderRequest.getOrderId();
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderInfo.setOrderStatus(OrderConstants.TO_START_PAY);
        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success("");
    }

    public ResponseResult pay(OrderRequest orderRequest) {
        Long orderId = orderRequest.getOrderId();
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);

        orderInfo.setOrderStatus(OrderConstants.OVER_PAY);
        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success("");
    }

    public ResponseResult cancel(Long orderId, String identity) {
        // 查询订单当前状态
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        Integer orderStatus = orderInfo.getOrderStatus();

        LocalDateTime cancelTime = LocalDateTime.now();
        Integer cancelOperator = null;
        Integer cancelTypeCode = null;

        // 正常取消
        int cancelType = 1;

        // 更新订单的取消状态
        // 如果是乘客取消
        if (identity.trim().equals(IdentityConstant.PASSENGER_IDENTITY)) {
            switch (orderStatus) {
                // 订单开始
                case OrderConstants.ORDER_START:
                    cancelTypeCode = OrderConstants.CANCEL_PASSENGER_BEFORE;
                    break;
                // 司机接到乘客
                case OrderConstants.DRIVER_RECEIVE_ORDER:
                    LocalDateTime receiveOrderTime = orderInfo.getReceiveOrderTime();
                    long between = ChronoUnit.MINUTES.between(receiveOrderTime, cancelTime);
                    if (between > 1) {
                        cancelTypeCode = OrderConstants.CANCEL_PASSENGER_ILLEGAL;
                    } else {
                        cancelTypeCode = OrderConstants.CANCEL_DRIVER_ILLEGAL;
                    }
                    break;
                // 司机去接乘客
                case OrderConstants.DRIVER_TO_PICK_UP_PASSENGER:
                    cancelTypeCode = OrderConstants.CANCEL_PASSENGER_ILLEGAL;
                    break;
                // 司机到达出发点
                case OrderConstants.DRIVER_ARRIVED_DEPARTURE:
                    cancelTypeCode = OrderConstants.CANCEL_PASSENGER_ILLEGAL;
                    break;
                default:
                    log.info("乘客取消失败");
                    cancelType = 0;
                    break;
            }
        }

        // 如果是司机取消
        if (identity.trim().equals(IdentityConstant.DRIVER_IDENTITY)) {
            switch (orderStatus) {
                // 司机接到乘客
                case OrderConstants.DRIVER_RECEIVE_ORDER:
                case OrderConstants.DRIVER_TO_PICK_UP_PASSENGER:
                case OrderConstants.DRIVER_ARRIVED_DEPARTURE:
                    LocalDateTime receiveOrderTime = orderInfo.getReceiveOrderTime();
                    long between = ChronoUnit.MINUTES.between(receiveOrderTime, cancelTime);
                    if (between > 1) {
                        cancelTypeCode = OrderConstants.CANCEL_DRIVER_ILLEGAL;
                    } else {
                        cancelTypeCode = OrderConstants.CANCEL_DRIVER_BEFORE;
                    }
                    break;
                default:
                    log.info("司机取消失败");
                    cancelType = 0;
                    break;
            }
        }

        if (cancelType == 0) {
            return ResponseResult.fail(CommonStatusEnum.ORDER_CANCEL_ERROR.getCode(), CommonStatusEnum.ORDER_CANCEL_ERROR.getValue());
        }

        orderInfo.setCancelTypeCode(cancelTypeCode);
        orderInfo.setCancelTime(cancelTime);
        orderInfo.setCancelOperator(Integer.parseInt(identity));
        orderInfo.setOrderStatus(OrderConstants.ORDER_CANCEL);

        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success("");
    }

    /**
     * 司机抢单
     * @param driverGrabRequest
     * @return
     */
    public ResponseResult grab(DriverGrabRequest driverGrabRequest) {
        Long orderId = driverGrabRequest.getOrderId();
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);

        Long driverId = driverGrabRequest.getDriverId();
        String driverPhone = driverGrabRequest.getDriverPhone();
        Long carId = driverGrabRequest.getCarId();
        String licenseId = driverGrabRequest.getLicenseId();
        String vehicleNo = driverGrabRequest.getVehicleNo();
        String receiveOrderCarLatitude = driverGrabRequest.getReceiveOrderCarLatitude();
        String receiveOrderCarLongitude = driverGrabRequest.getReceiveOrderCarLongitude();
        String vehicleType = driverGrabRequest.getVehicleType();

        orderInfo.setDriverId(driverId);
        orderInfo.setDriverPhone(driverPhone);
        orderInfo.setCarId(carId);
        orderInfo.setLicenseId(licenseId);
        orderInfo.setVehicleNo(vehicleNo);
        orderInfo.setReceiveOrderCarLatitude(receiveOrderCarLatitude);
        orderInfo.setReceiveOrderCarLongitude(receiveOrderCarLongitude);
        orderInfo.setVehicleType(vehicleType);
        orderInfo.setOrderStatus(OrderConstants.DRIVER_RECEIVE_ORDER);
        orderInfo.setReceiveOrderTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));

        orderInfoMapper.updateById(orderInfo);

        return ResponseResult.success("");

    }
}
