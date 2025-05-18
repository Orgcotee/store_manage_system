<template>
  <view class="container">
    <!-- 顶部导航栏 -->
  <!--  <view class="nav-bar">
      <view class="close-btn" @click="handleClose">×</view>
      <view class="title">微信支付</view>
    </view> -->

    <!-- 支付主体 -->
    <view class="payment-body">
      <!-- 收款方信息 -->
      <view class="payee-info">
        <text class="label">收款方</text>
        <text class="name">商户名称</text>
      </view>

      <!-- 支付金额 -->
      <view class="amount">
        <text class="symbol">¥</text>
        <text class="number">{{ amount }}</text>
      </view>

      <!-- 支付方式选择 -->
      <view class="payment-methods">
        <view class="method-item" @click="selectMethod('balance')">
          <image class="method-icon" src="/static/wechat-balance.png"></image>
          <view class="method-info">
            <text class="method-name">零钱</text>
            <text class="method-balance">余额：¥{{ balance }}</text>
          </view>
          <radio :checked="selectedMethod === 'balance'" color="#09BB07"/>
        </view>

        <view class="method-item" @click="selectMethod('card')">
          <image class="method-icon" src="/static/bank-card.png"></image>
          <view class="method-info">
            <text class="method-name">银行卡</text>
            <text class="card-number">尾号1234</text>
          </view>
          <radio :checked="selectedMethod === 'card'" color="#09BB07"/>
        </view>
      </view>
    </view>

    <!-- 支付按钮 -->
    <view class="payment-footer">
      <view 
        class="pay-btn" 
        :class="{ disabled: paying }" 
        @click="handlePayment"
      >
        {{ paying ? '支付中...' : `确认支付 ¥${amount}` }}
      </view>
      <view class="security-tips">
        <image class="security-icon" src="/static/security.png"></image>
        微信支付安全认证
      </view>
    </view>

    <!-- 支付密码输入弹窗 -->
    <view class="password-modal" v-if="showPasswordModal">
      <view class="modal-content">
        <view class="modal-header">
          <text class="title">输入支付密码</text>
          <text class="close" @click="showPasswordModal = false">×</text>
        </view>
        <view class="password-input">
          <input 
            type="password" 
            v-model="password" 
            maxlength="6"
            placeholder="请输入6位支付密码"
            focus
          />
          <view class="fake-input">
            <view 
              class="dot" 
              v-for="(item, index) in 6" 
              :key="index"
            >{{ password.length > index ? '•' : '' }}</view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      amount: 299.00,
      balance: 1500.00,
      selectedMethod: 'balance',
      paying: false,
      showPasswordModal: false,
      password: ''
    }
  },
  watch: {
    password(newVal) {
      if (newVal.length === 6) {
        this.submitPayment()
      }
    }
  },
  methods: {
    handleClose() {
      uni.navigateBack()
    },
    selectMethod(method) {
      this.selectedMethod = method
    },
    handlePayment() {
      if (!this.paying) {
        this.showPasswordModal = true
      }
    },
    async submitPayment() {
      this.paying = true
      
      // 模拟支付请求
      try {
        await new Promise(resolve => setTimeout(resolve, 1500))
        
        uni.redirectTo({
          url: '/pages/payment/success?amount=' + this.amount
        })
      } catch (error) {
        uni.showToast({
          title: '支付失败',
          icon: 'none'
        })
      } finally {
        this.paying = false
        this.password = ''
        this.showPasswordModal = false
      }
    }
  }
}
</script>

<style scoped>
/* 微信风格配色 */
:root {
  --wechat-green: #09BB07;
  --wechat-bg: #F5F5F5;
}

.container {
  background: var(--wechat-bg);
  min-height: 100vh;
}

/* 导航栏样式 */
.nav-bar {
  height: 90rpx;
  background: #FFFFFF;
  display: flex;
  align-items: center;
  padding: 0 30rpx;
  border-bottom: 1rpx solid #EEE;
}

.close-btn {
  font-size: 50rpx;
  color: #666;
  width: 60rpx;
}

.title {
  flex: 1;
  text-align: center;
  font-size: 36rpx;
  font-weight: 500;
}

/* 支付主体 */
.payment-body {
  padding: 40rpx 30rpx;
}

.payee-info {
  text-align: center;
  margin-bottom: 60rpx;
}

.label {
  display: block;
  color: #888;
  font-size: 28rpx;
  margin-bottom: 10rpx;
}

.name {
  font-size: 32rpx;
  color: #333;
}

/* 金额显示 */
.amount {
  text-align: center;
  margin-bottom: 80rpx;
}

.symbol {
  font-size: 48rpx;
  color: #333;
  vertical-align: top;
}

.number {
  font-size: 64rpx;
  font-weight: bold;
  color: #333;
  margin-left: 10rpx;
}

/* 支付方式 */
.payment-methods {
  background: #FFF;
  border-radius: 16rpx;
  overflow: hidden;
}

.method-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #EEE;
}

.method-item:last-child {
  border-bottom: none;
}

.method-icon {
  width: 60rpx;
  height: 60rpx;
  margin-right: 30rpx;
}

.method-info {
  flex: 1;
}

.method-name {
  display: block;
  font-size: 32rpx;
  color: #333;
  margin-bottom: 8rpx;
}

.method-balance, .card-number {
  font-size: 26rpx;
  color: #999;
}

/* 支付按钮 */
.payment-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30rpx;
  background: #FFF;
}

.pay-btn {
  height: 90rpx;
  background: var(--wechat-green);
  border-radius: 45rpx;
  color: #FFF;
  font-size: 36rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.pay-btn.disabled {
  background: #CCC;
}

.security-tips {
  text-align: center;
  color: #999;
  font-size: 24rpx;
  margin-top: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.security-icon {
  width: 24rpx;
  height: 24rpx;
  margin-right: 8rpx;
}

/* 密码输入弹窗 */
.password-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  background: #FFF;
  width: 80%;
  border-radius: 16rpx;
  overflow: hidden;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  padding: 30rpx;
  border-bottom: 1rpx solid #EEE;
}

.modal-header .title {
  font-size: 32rpx;
  color: #333;
}

.modal-header .close {
  font-size: 40rpx;
  color: #666;
}

.password-input {
  padding: 40rpx;
  position: relative;
}

.password-input input {
  position: absolute;
  opacity: 0;
  width: 100%;
  height: 100rpx;
}

.fake-input {
  display: flex;
  height: 100rpx;
  border: 1rpx solid #EEE;
  border-radius: 8rpx;
}

.fake-input .dot {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 50rpx;
  border-right: 1rpx solid #EEE;
}

.fake-input .dot:last-child {
  border-right: none;
}
</style>