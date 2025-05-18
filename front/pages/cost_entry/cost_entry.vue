<template>
  <view class="container">
    <view class="form-container">
      <view class="section-title">门店日营业额录入</view>
      <scroll-view scroll-y class="form-content">
        <!-- 门店信息 -->
        <view class="form-section">
          <view class="form-item">
            <text class="label required">所属门店</text>
            <picker 
              class="input"
              :range="storeOptions"
              range-key="label"
              @change="onStoreChange"
            >
              <view class="picker-text">{{ selectedStore.label || '请选择门店' }}</view>
            </picker>
          </view>
          
          <view class="form-item">
            <text class="label">记录人员</text>
            <input 
              class="input disabled" 
              :value="currentUser"
              disabled
              placeholder="自动获取"
            />
          </view>
          
          <view class="form-item">
            <text class="label">记录日期</text>
            <input 
              class="input disabled"
              :value="currentTime"
              disabled
            />
          </view>
        </view>

        <!-- 收入信息 -->
        <view class="form-section">
          <view class="section-header">
            <text class="sub-title">收入明细</text>
            <text class="amount-label">总收入：￥{{ totalIncome }}</text>
          </view>
          
          <view class="form-item">
            <text class="label required">收入金额</text>
            <input
              class="input"
              type="number"
              v-model="formData.income"
              placeholder="请输入金额"
            />
          </view>
          
          <view class="form-item">
            <text class="label required">收入说明</text>
            <textarea
              class="textarea"
              v-model="formData.inDetail"
              placeholder="例：餐饮收入、商品销售..."
              maxlength="100"
            />
            <text class="word-count">{{ formData.inDetail.length }}/100</text>
          </view>
        </view>

        <!-- 支出信息 -->
        <view class="form-section">
          <view class="section-header">
            <text class="sub-title">支出明细</text>
            <text class="amount-label">总支出：￥{{ totalOutcome }}</text>
          </view>
          
          <view class="form-item">
            <text class="label required">支出金额</text>
            <input
              class="input"
              type="number"
              v-model="formData.outcome"
              placeholder="请输入金额"
            />
          </view>
          
          <view class="form-item">
            <text class="label required">支出说明</text>
            <textarea
              class="textarea"
              v-model="formData.outDetail"
              placeholder="例：食材采购、设备维护..."
              maxlength="100"
            />
            <text class="word-count">{{ formData.outDetail.length }}/100</text>
          </view>
        </view>

        <!-- 报损信息 -->
        <view class="form-section">
          <view class="section-header">
            <text class="sub-title">报损明细</text>
            <text class="amount-label">总报损：￥{{ formData.frmloss || 0 }}</text>
          </view>
          
          <view class="form-item">
            <text class="label required">报损金额</text>
            <input
              class="input"
              type="number"
              v-model="formData.frmloss"
              placeholder="请输入金额"
            />
          </view>
          
          <view class="form-item">
            <text class="label required">报损说明</text>
            <textarea
              class="textarea"
              v-model="formData.frmDetail"
              placeholder="例：食材过期损耗、设备故障..."
              maxlength="100"
            />
            <text class="word-count">{{ formData.frmDetail.length }}/100</text>
          </view>
        </view>

        <!-- 经营概览 -->
        <view class="form-section highlight-section">
          <view class="section-header">
            <text class="sub-title">本日经营概览</text>
          </view>
          <view class="summary-item">
            <text class="summary-label">今日利润：</text>
            <text :class="['summary-value', { positive: todayProfit >= 0 }]">
              ￥{{ todayProfit.toFixed(2) }}
            </text>
          </view>
          <view class="summary-item">
            <text class="summary-label">累计报损：</text>
            <text class="summary-value negative">￥{{ Number(formData.frmloss || 0).toFixed(2) }}</text>
          </view>
        </view>
      </scroll-view>

      <!-- 提交按钮 -->
      <view class="submit-container">
        <button 
          class="submit-btn"
          @click="handleSubmit"
          :disabled="!formValid"
        >
          <text v-if="formValid">提交日报</text>
          <text v-else class="disabled-text">请填写必填信息</text>
        </button>
      </view>
    </view>

	<uni-popup ref="confirmPopup" type="dialog">
		<uni-popup-dialog 
			mode="base" 
			title="操作确认" 
			content="请确认无误，提交后数据将发送给总部系统！"
			:duration="2000" 
			:before-close="true" 
			@confirm="handleConfirm" 
			@close="handleCancel"
		></uni-popup-dialog>
	</uni-popup>
    <!-- 操作反馈 -->
    <uni-popup ref="messagePopup" type="message">
      <uni-popup-message :type="messageType" :message="messageText" />
    </uni-popup>
  </view>
</template>

<script>
import { globalURL } from '../../constant/config.js'

export default {
  data() {
    return {
      currentUser: "李店长",
      currentTime: this.getCurrentTime(),
      formData: {
        store: null,
        income: null,
        inDetail: "",
        outcome: null,
        outDetail: "",
        frmloss: null,
        frmDetail: ""
      },
      storeOptions: [
        { label: "中山路旗舰店", value: 1 },
        { label: "浦东新区分店", value: 2 }
      ],
      selectedStore: {},
      messageType: "success",
      messageText: ""
    };
  },
  computed: {
    totalIncome() {
      return (Number(this.formData.income) || 0).toFixed(2);
    },
    totalOutcome() {
      return (Number(this.formData.outcome) || 0).toFixed(2);
    },
    todayProfit() {
      return (this.formData.income || 0) - (this.formData.outcome || 0);
    },
    formValid() {
      return (
        this.formData.store !== null &&
        (
          this.formData.income !== null ||
          this.formData.outcome !== null ||
          this.formData.frmloss !== null
        ) &&
        // 校验说明字段
        (this.formData.income === null || this.formData.inDetail) &&
        (this.formData.outcome === null || this.formData.outDetail) &&
        (this.formData.frmloss === null || this.formData.frmDetail)
      );
    }
  },
  methods: {
    onStoreChange(e) {
      this.selectedStore = this.storeOptions[e.detail.value];
      this.formData.store = this.selectedStore.value;
    },
    getCurrentTime() {
      const now = new Date();
      return `${now.getFullYear()}-${(now.getMonth()+1).toString().padStart(2,'0')}-${now.getDate().toString().padStart(2,'0')} ${now.getHours().toString().padStart(2,'0')}:${now.getMinutes().toString().padStart(2,'0')}`;
    },
    async handleSubmit() {
      if (!this.formValid) return;
      
	  // 显示确认弹窗
      this.$refs.confirmPopup.open();
	  
    },
	 // 确认处理方法
	    async handleConfirm() {
	      try {
	        console.log('提交数据:', JSON.stringify({
	          ...this.formData,
	          recorder: this.currentUser
	        }));
	        
	        const res = await uni.request({
	          url: `${globalURL}/api/fStoreRecord/daily`,
	          method: 'POST',
	          data: {
	            ...this.formData,
	            recorder: this.currentUser
	          },
	          header: {
	            'Content-Type': 'application/json'
	          },
	        });
	
	        if (res.statusCode === 200) {
	          this.showMessage("日报提交成功", "success");
	          this.resetForm();
	        } else {
	          console.error('错误响应:', res);
	          this.showMessage("提交失败：" + res.data.msg, "error");
	        }
	      } catch (err) {
	        console.error('请求异常:', err);
	        this.showMessage("网络错误，请重试", "error");
	      } finally {
	        this.$refs.confirmPopup.close();
	      }
	    },
	
	    // 取消处理方法
	    handleCancel() {
	      console.log('用户取消提交');
	      this.$refs.confirmPopup.close();
	    },
    resetForm() {
      this.formData = {
        store: null,
        income: null,
        inDetail: "",
        outcome: null,
        outDetail: "",
        frmloss: null,
        frmDetail: ""
      };
      this.selectedStore = {};
    },
    showMessage(text, type) {
      this.messageText = text;
      this.messageType = type;
      this.$refs.messagePopup.open();
      setTimeout(() => this.$refs.messagePopup.close(), 1500);
    }
  }
};
</script>

<style scoped>
/* 新增样式 */
.highlight-section {
  border-color: #2196F3;
  background: #f5fbff;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #eee;
}

.summary-label {
  font-size: 28rpx;
  color: #666;
}

.summary-value {
  font-size: 32rpx;
  font-weight: bold;
}

.positive {
  color: #4CAF50;
}

.negative {
  color: #F44336;
}

.highlight-section .sub-title {
  color: #2196F3;
}

.container {
  padding: 20rpx;
  background-color: #f8f9fa;
}

.form-container {
  background: white;
  border-radius: 12rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 36rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 40rpx;
  padding-left: 15rpx;
  border-left: 8rpx solid #2196F3;
}

.form-content {
  max-height: 75vh;
}

.form-section {
  margin-bottom: 40rpx;
  padding: 20rpx;
  border-radius: 8rpx;
  border: 2rpx solid #eee;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.sub-title {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
}

.amount-label {
  color: #ff9800;
  font-size: 28rpx;
  font-weight: bold;
}

.form-item {
  margin-bottom: 35rpx;
}

.label {
  display: block;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 15rpx;
}

.required::after {
  content: "*";
  color: #f56c6c;
  margin-left: 8rpx;
}

.input {
  width: 100%;
  height: 80rpx;
  padding: 0 20rpx;
  border: 2rpx solid #eee;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.disabled {
  background-color: #f5f7fa;
  color: #c0c4cc;
}

.textarea {
  width: 100%;
  height: 180rpx;
  padding: 20rpx;
  border: 2rpx solid #eee;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.word-count {
  display: block;
  text-align: right;
  color: #999;
  font-size: 24rpx;
  margin-top: 10rpx;
}

.submit-container {
  margin-top: 50rpx;
  padding: 0 20rpx;
}

.submit-btn {
  height: 90rpx;
  line-height: 90rpx;
  border-radius: 8rpx;
  background: #2196F3;
  color: white;
  font-size: 32rpx;
}

.submit-btn[disabled] {
  background: #f5f7fa;
}

.disabled-text {
  color: #c0c4cc;
}

.picker-text {
  height: 80rpx;
  line-height: 80rpx;
  color: #333;
}
</style>