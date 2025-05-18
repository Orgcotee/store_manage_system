<template>
  <view class="container">
    <!-- 成功动画 -->
    <view class="success-icon">
      <view class="checkmark"></view>
    </view>

    <!-- 成功提示 -->
    <view class="success-text">
      <text class="title">支付成功！</text>
      <text class="amount">¥ {{paymentData.amount}}</text>
    </view>

    <!-- 订单信息 -->
    <view class="order-info">
      <view class="info-item">
        <text class="label">支付方式</text>
        <text class="value">{{paymentData.paymentMethod}}</text>
      </view>
      <view class="info-item">
        <text class="label">订单编号</text>
        <text class="value">{{paymentData.orderNo}}</text>
      </view>
      <view class="info-item">
        <text class="label">支付时间</text>
        <text class="value">{{paymentData.paymentTime}}</text>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-btns">
      <view class="btn back-home" @click="goHome">返回首页</view>
      <view class="btn view-order" @click="viewOrder">查看订单</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      paymentData: {
        amount: 299.00,
        paymentMethod: "支付宝",
        orderNo: "20231234567890",
        paymentTime: "2023-12-20 14:30:45"
      }
    }
  },
  methods: {
    goHome() {
      uni.switchTab({
        url: '/pages/index/index'
      })
    },
    viewOrder() {
      uni.navigateTo({
        url: '/pages/order/detail'
      })
    }
  }
}
</script>

<style scoped>
.container {
  padding: 40rpx;
  background: #f8f9fa;
  min-height: 100vh;
}

/* 成功图标动画 */
.success-icon {
  width: 160rpx;
  height: 160rpx;
  background: #07c160;
  border-radius: 50%;
  margin: 80rpx auto;
  position: relative;
  animation: scaleUp 0.6s ease-in-out;
}

.checkmark {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 60rpx;
  height: 100rpx;
  border: solid #fff;
  border-width: 0 8rpx 8rpx 0;
  transform: translate(-50%, -60%) rotate(45deg);
}

@keyframes scaleUp {
  0% { transform: scale(0); }
  60% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

/* 成功文字 */
.success-text {
  text-align: center;
  margin-bottom: 60rpx;
}

.title {
  display: block;
  font-size: 44rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 20rpx;
}

.amount {
  font-size: 48rpx;
  color: #07c160;
  font-weight: bold;
}

/* 订单信息 */
.order-info {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);
}

.info-item {
  display: flex;
  justify-content: space-between;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #eee;
}

.info-item:last-child {
  border-bottom: none;
}

.label {
  color: #666;
  font-size: 28rpx;
}

.value {
  color: #333;
  font-size: 30rpx;
  font-weight: 500;
}

/* 操作按钮 */
.action-btns {
  margin-top: 80rpx;
  display: flex;
  gap: 30rpx;
  padding: 0 30rpx;
}

.btn {
  flex: 1;
  height: 90rpx;
  border-radius: 45rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  transition: all 0.2s;
}

.back-home {
  background: #fff;
  color: #333;
  border: 1rpx solid #ddd;
}

.back-home:active {
  background: #f8f8f8;
}

.view-order {
  background: #07c160;
  color: #fff;
}

.view-order:active {
  background: #06ad54;
}
</style>