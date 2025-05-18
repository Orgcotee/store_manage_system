<template>
  <view class="container">
    <!-- 报账表单 -->
    <view class="form-container">

	<view class="form-item">
	  <text class="form-label">申请人：</text>
	  <view class="picker disabled-text">
		{{ loginer || '未获取到申请人信息' }}
	  </view>
	</view>
	
	<view class="form-item">
	  <text class="form-label">工号：</text>
	  <view class="picker disabled-text">
		{{ loginerId || '未获取到申请人工号' }}
	  </view>
	</view>
	
      <view class="form-item">
        <text class="form-label">报账类型：</text>
        <picker 
          @change="selectType" 
          :value="typeIndex" 
          :range="reimburseTypes"
        >
          <view class="picker">
            {{ selectedType || '请选择类型' }}
          </view>
        </picker>
      </view>


      <view class="form-item">
        <text class="form-label">金额（元）：</text>
        <input 
          type="number" 
          v-model="formData.amount" 
          placeholder="请输入金额"
          class="input"
        />
      </view>

      <view class="form-item">
        <text class="form-label">报账详情：</text>
        <textarea 
          v-model="formData.details" 
          placeholder="请输入详细报账内容"
          class="textarea"
          :maxlength="200"
        />
      </view>

      <button class="submit-btn" @tap="submitApplication">提交申请</button>
    </view>

    <!-- 历史记录 -->
    <view class="history-container">
      <view class="section-title">报账记录</view>
      
      <scroll-view scroll-y class="list-container">
        <view 
          v-for="(item, index) in applications" 
          :key="index" 
          class="list-item"
        >
          <view class="info-box">
            <view class="header">
              <text class="applicant">{{ item.employeeId }}{{ item.employeeName }}</text>
              <text :class="['status', item.approvalStatus]">{{ statusMap[item.approvalStatus] }}</text>
            </view>
            <view class="detail-row">
              <text class="label">类型：</text>
              <text class="value">{{ item.reimburseType }}</text>
            </view>
            <view class="detail-row">
              <text class="label">金额：</text>
              <text class="amount">￥{{ item.amount }}</text>
            </view>
            <view class="meta">
              <text class="time">{{ item.applyTime }}</text>
              <text class="approver" v-if="item.approver">审批人：{{ item.approver }}</text>
            </view>
            <view class="details" v-if="item.details">详情：{{ item.details }}</view>
            <view class="remark-approved" v-if="item.remark != null && item.approvalStatus === 'approved'">审批意见：{{item.remark }}</view>
			<view class="remark-rejected" v-if="item.remark != null && item.approvalStatus === 'rejected'">审批意见：{{item.remark }}</view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 提交提示 -->
    <uni-popup ref="successPopup" type="message">
      <uni-popup-message type="success" message="提交成功" />
    </uni-popup>
  </view>
</template>

<script>
import { globalURL } from '../../constant/config.js'
export default {
  data() {
    return {
	  loginer:'张伟',
	  loginerId:'1015',
      employees: [],
      reimburseTypes: ['物品损坏', '日用品损坏', '消耗品'],
      selectedIndex: -1,
      typeIndex: -1,
      formData: {
        employeeId: '',
        employeeName: '',
        reimburseType: '',
        amount: '',
        details: '',
		img:'',
		approvalStatus: 0,
		applicationTime: ''
      },
      applications: [],
      statusMap: {
        pending: '审批中',
        approved: '已通过',
        rejected: '已驳回'
      }
    }
  },
  async created() {
      await this.loadEmployees();
    },
	created() {
	  this.loadEmployees();
	  this.loadApplications();
	},
  computed: {
    selectedEmployee() {
      return this.selectedIndex >= 0 
        ? this.employees[this.selectedIndex]
        : {}
    },
    selectedType() {
      return this.typeIndex >= 0
        ? this.reimburseTypes[this.typeIndex]
        : ''
    }
  },
  methods: {
	  getStatusText(status) {
	    const map = {0: 'pending', 1: 'approved', 2: 'rejected'};
	    return map[status];
	  },
	  // 改成按部门查找 **
	  async loadEmployees() {
	        const res = await uni.request({
	          url: `${globalURL}/api/fStaff`,
	          method: 'GET'
	        });
	        this.employees = res.data.data.map(item => ({
	          id: item.id,
	          name: item.name,
	          department: item.department
	        }));
	      },
		  // 需改成根据登录人员姓名所在部门查找申请记录**
	async loadApplications() {
	  try {
	    const res = await uni.request({
	      url: `${globalURL}/api/reimbursement/applicantId/${this.loginerId}`,
	      method: 'GET'
	    });
	    console.log("加载申请记录：",res.data.data);
	    this.applications = res.data.data.map(item => ({
	      id: item.id,
		  employeeId: item.applicantId,
	      employeeName: item.applicant,
	      reimburseType: item.category,
	      amount: item.amount,
	      details: item.details,
	      applyTime: this.formatDate(item.applicationTime),
	      // approvalStatus: this.getStatusText(item.status),
		  approvalStatus: item.status,
	      approver: item.approver,
	      remark: item.approvalOpinion
	    }));
	  } catch (e) {
	    console.error('加载记录失败:', e);
	  }
	},
	async submitApplication() {
	  if (!this.validateForm()) return;
	
	  const payload = {
		applicantId: this.loginerId,
	    applicant: this.loginer,
	    category: this.formData.reimburseType,
	    inOut: 1, // 对应后端的1支出/0收入
	    amount: Number(this.formData.amount).toFixed(2),
	    details: this.formData.details,
	    img: "", // 需要补充图片上传功能
	    approvalStatus: 0, // 0-待审批状态
	    applicationTime: new Date()
	  };
	
	  try {
	    const res = await uni.request({
	      url: `${globalURL}/api/reimbursement/create`,
	      method: 'POST',
	      data: payload,
	      header: { 'Content-Type': 'application/json' }
	    });
	    
	    if (res.statusCode === 200) {
			// 前端手动插入新数据
	      // this.applications.unshift({
	      //   ...payload,
	      //   id: res.data.data,
	      //   applyTime: this.formatDate(new Date()),
	      //   status: 'pending',
	      //   approvalStatus: 0
	      // });
	      this.resetForm();
	      this.$refs.successPopup.open();
		  
		  await this.loadApplications();
	    }
	  } catch (e) {
	    uni.showToast({ title: '提交失败，请重试', icon: 'none' });
	  }
	},
    selectEmployee(e) {
      this.selectedIndex = e.detail.value
      this.formData.employeeId = this.employees[this.selectedIndex].id
      this.formData.employeeName = this.employees[this.selectedIndex].name
    },
    selectType(e) {
      this.typeIndex = e.detail.value
      this.formData.reimburseType = this.reimburseTypes[this.typeIndex]
    },
    validateForm() {
      // if (!this.formData.employeeId) {
      //   uni.showToast({ title: '请选择申请人', icon: 'none' })
      //   return false
      // }
      if (!this.formData.reimburseType) {
        uni.showToast({ title: '请选择报账类型', icon: 'none' })
        return false
      }
      if (!this.formData.amount || Number(this.formData.amount) <= 0) {
        uni.showToast({ title: '请输入有效金额', icon: 'none' })
        return false
      }
      if (!this.formData.details.trim()) {
        uni.showToast({ title: '请输入报账详情', icon: 'none' })
        return false
      }
      return true
    },
	// 添加图片上传方法
	async uploadImage() {
	  const res = await uni.chooseImage();
	  const file = res.tempFilePaths[0];
	  const uploadRes = await uni.uploadFile({
	    url: `${globalURL}/api/upload`,
	    filePath: file,
	    name: 'file'
	  });
	  this.formData.img = JSON.parse(uploadRes.data).data;
	},
    // submitApplication() {
    //   if (!this.validateForm()) return

    //   const newApplication = {
    //     id: Date.now(),
    //     ...this.formData,
    //     applyTime: this.getCurrentTime(),
    //     status: 'pending'
    //   }

    //   this.applications.unshift(newApplication)
    //   this.resetForm()
    //   this.$refs.successPopup.open()
    //   setTimeout(() => this.$refs.successPopup.close(), 1500)
    // },
    formatDate(date) {
      const d = new Date(date);
      return `${d.getFullYear()}-${(d.getMonth()+1).toString().padStart(2,'0')}-${d.getDate().toString().padStart(2,'0')} ${d.getHours().toString().padStart(2,'0')}:${d.getMinutes().toString().padStart(2,'0')}`;
    },
    resetForm() {
      this.selectedIndex = -1
      this.typeIndex = -1
      this.formData = {
        employeeId: '',
        employeeName: '',
        reimburseType: '',
        amount: '',
        details: ''
      }
    }
  }
}
</script>

<style scoped>
/* 新增置灰样式 */
.disabled-text {
  color: #999 !important;         /* 浅灰色文字 */
  background-color: #f5f5f5;      /* 浅灰色背景 */
  cursor: not-allowed;            /* 禁用光标 */
  opacity: 0.9;                   /* 轻微透明效果 */
}
.container {
  padding: 20rpx;
  background-color: #f8f9fa;
}

.form-container {
  background: white;
  padding: 30rpx;
  border-radius: 12rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.05);
}

.form-item {
  margin-bottom: 30rpx;
}

.form-label {
  display: block;
  color: #666;
  font-size: 28rpx;
  margin-bottom: 15rpx;
}

.picker, .input, .textarea {
  width: 100%;
  height: 80rpx;
  padding: 0 20rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.textarea {
  height: 200rpx;
  padding: 20rpx;
}

.submit-btn {
  background: #2196F3;
  color: white;
  height: 90rpx;
  line-height: 90rpx;
  border-radius: 8rpx;
  font-size: 32rpx;
  margin-top: 40rpx;
}

.history-container {
  background: white;
  border-radius: 12rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 30rpx;
  padding-left: 10rpx;
  border-left: 6rpx solid #2196F3;
}

.list-container {
  max-height: 60vh;
}

.list-item {
  padding: 25rpx;
  margin-bottom: 25rpx;
  background: #fff;
  border-radius: 8rpx;
  border: 1rpx solid #eee;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.applicant {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
}

.status {
  font-size: 24rpx;
  padding: 6rpx 15rpx;
  border-radius: 4rpx;
}

.status.pending {
  background: #e3f2fd;
  color: #2196F3;
}

.status.approved {
  background: #e8f5e9;
  color: #4CAF50;
}

.status.rejected {
  background: #ffebee;
  color: #f44336;
}

.detail-row {
  display: flex;
  align-items: center;
  margin: 10rpx 0;
}

.label {
  color: #666;
  font-size: 26rpx;
  min-width: 120rpx;
}

.value {
  color: #333;
  font-size: 26rpx;
}

.amount {
  color: #ff9800;
  font-size: 30rpx;
  font-weight: bold;
}

.meta {
  display: flex;
  justify-content: space-between;
  color: #999;
  font-size: 24rpx;
  margin-top: 15rpx;
}

.details, .remark {
  color: #666;
  font-size: 26rpx;
  margin-top: 15rpx;
  line-height: 1.6;
}

.remark-rejected {
  color: #f44336;
}

.remark-approved {
  color: #666;
}
</style>