<template>
  <div class="payment-container">
    <!-- 支付头部 -->
    <div class="header">
      <div class="logo">支付宝</div>
      <div class="amount-display">
        ￥{{ amount.toFixed(2) }}
      </div>
      <div class="order-info">订单号：{{ orderId }}</div>
    </div>

    <!-- 密码输入区域 -->
    <div class="password-input">
      <div 
        v-for="i in 6" 
        :key="i"
        class="input-item"
        :class="{ 'active': currentIndex === i-1 }"
      >
        {{ password[i-1] ? '•' : '' }}
        <input
          v-model="password[i-1]"
          type="password"
          maxlength="1"
          @input="handleInput(i-1)"
          @keydown.delete="handleDelete(i-1)"
          :ref="'input' + i"
        >
      </div>
    </div>

    <!-- 操作按钮 -->
    <button 
      class="pay-button"
      :disabled="processing"
      @click="handlePayment"
    >
      {{ processing ? '支付中...' : '确认支付' }}
    </button>

    <!-- 支付结果弹窗 -->
    <div v-if="showResult" class="result-modal">
      <div class="result-content">
        <div class="icon" :class="paymentStatus">{{ paymentStatus === 'success' ? '✓' : '✕' }}</div>
        <h3>{{ resultMessage }}</h3>
        <button @click="closeModal">确定</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 订单信息
      orderId: this.$route.query.orderId || '202308310001',
      amount: 0,
      
      // 密码输入相关
      password: Array(6).fill(''),
      currentIndex: 0,
      processing: false,
      showResult: false,
      paymentStatus: '',
      resultMessage: ''
    }
  },
  mounted() {
    this.fetchOrderAmount()
    this.$nextTick(() => {
      this.focusInput(0)
    })
  },
  methods: {
    // 模拟获取订单金额
    async fetchOrderAmount() {
      // 实际项目中应调用API接口
      await new Promise(resolve => setTimeout(resolve, 500))
      this.amount = Math.random() * 100 + 50 // 模拟金额
    },

    // 处理输入
    handleInput(index) {
      if (this.password[index]) {
        this.currentIndex = Math.min(index + 1, 5)
        this.focusInput(this.currentIndex)
      }
    },

    // 处理删除
    handleDelete(index) {
      if (index > 0 && !this.password[index]) {
        this.currentIndex = index - 1
        this.focusInput(this.currentIndex)
      }
    },

    // 聚焦输入框
    focusInput(index) {
      this.$nextTick(() => {
        this.$refs['input' + (index + 1)][0].focus()
      })
    },

    // 处理支付
    async handlePayment() {
      this.processing = true
      try {
        // 模拟支付API调用
        await new Promise((resolve, reject) => {
          setTimeout(() => {
            Math.random() > 0.2 ? resolve() : reject()
          }, 1000)
        })

        this.paymentStatus = 'success'
        this.resultMessage = '支付成功！'
      } catch (error) {
        this.paymentStatus = 'error'
        this.resultMessage = '支付失败，请重试'
      } finally {
        this.showResult = true
        this.processing = false
        this.password = Array(6).fill('')
        this.currentIndex = 0
      }
    },

    // 关闭弹窗
    closeModal() {
      this.showResult = false
      if (this.paymentStatus === 'success') {
        this.$router.push('/orders')
      }
    }
  }
}
</script>

<style scoped>
/* 保持样式不变 */
.payment-container {
  max-width: 400px;
  margin: 20px auto;
  padding: 20px;
  background: #fff;
}

.header {
  text-align: center;
  padding: 20px 0;
}

.logo {
  color: #1677ff;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 15px;
}

.amount-display {
  font-size: 32px;
  color: #333;
  margin: 20px 0;
}

.order-info {
  color: #999;
  font-size: 14px;
}

.password-input {
  display: flex;
  justify-content: center;
  margin: 30px 0;
}

.input-item {
  width: 40px;
  height: 40px;
  margin: 0 5px;
  border: 1px solid #ddd;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.input-item.active {
  border-color: #1677ff;
}

.input-item input {
  position: absolute;
  width: 100%;
  height: 100%;
  opacity: 0;
  border: none;
  text-align: center;
  font-size: 18px;
}

.pay-button {
  width: 100%;
  padding: 12px;
  background: #1677ff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.pay-button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.result-modal {
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

.result-content {
  background: white;
  padding: 30px;
  border-radius: 8px;
  text-align: center;
}

.icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  font-size: 32px;
  line-height: 50px;
  margin: 0 auto 20px;
}

.icon.success {
  color: #52c41a;
  border: 2px solid #52c41a;
}

.icon.error {
  color: #ff4d4f;
  border: 2px solid #ff4d4f;
}
</style>