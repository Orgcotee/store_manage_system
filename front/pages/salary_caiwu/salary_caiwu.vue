<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view class="search-box">
      <input 
        v-model="searchName" 
        placeholder="请输入姓名搜索" 
        class="search-input"
        placeholder-style="color:#999"
      />
      <button @tap="refreshList" class="refresh-btn">刷新列表</button>
    </view>

    <!-- 工资列表 -->
    <scroll-view scroll-y class="list-container">
      <view 
        v-for="(item, index) in filteredEmployees" 
        :key="index" 
        class="list-item"
      >
        <view class="info-container">
          <view class="header">
            <text class="name">{{ item.name }}</text>
            <text :class="['status', item.status === '在职' ? 'active' : 'leave']">
              {{ item.status }}
            </text>
          </view>
          <view class="details">
            <text class="department">{{ item.department }}</text>
            <text class="separator">|</text>
            <text class="salary">税前：{{ item.preTaxSalary }}元</text>
            <text class="separator">|</text>
            <text class="salary">税后：{{ item.postTaxSalary }}元</text>
          </view>
        </view>
        <view class="action-container">
          <button 
            v-if="!item.paid" 
            @tap="paySalary(index)" 
            class="action-btn pay"
          >
            发放
          </button>
          <text v-else class="paid-text">已发放</text>
          <button @tap="showDetail(item)" class="action-btn detail">详情</button>
        </view>
      </view>
    </scroll-view>

    <!-- 工资详情弹窗 -->
    <uni-popup ref="detailPopup" type="dialog">
      <uni-popup-dialog title="工资明细" :show-cancel="false" confirm-text="关闭">
        <view class="detail-container">
          <view class="compact-columns">
            <view class="compact-column">
              <view class="detail-section">
                <text class="section-title">基本信息</text>
                <view class="detail-item">
                  <text class="detail-label">姓名：</text>
                  <text class="detail-value">{{ currentDetail.name }}</text>
                </view>
                <view class="detail-item">
                  <text class="detail-label">部门：</text>
                  <text class="detail-value">{{ currentDetail.department }}</text>
                </view>
                <view class="detail-item">
                  <text class="detail-label">发放状态：</text>
                  <text :class="['detail-value', currentDetail.paid ? 'paid' : 'unpaid']">
                    {{ currentDetail.paid ? '已发放' : '未发放' }}
                  </text>
                </view>
              </view>
            </view>
        
            <view class="compact-column">
              <view class="detail-section">
                <text class="section-title">薪资构成</text>
                <view class="detail-item">
                  <text class="detail-label">基本工资：</text>
                  <text class="detail-value">{{ currentDetail.salaryDetails.baseSalary }}元</text>
                </view>
                <view class="detail-item">
                  <text class="detail-label">绩效奖金：</text>
                  <text class="detail-value">{{ currentDetail.salaryDetails.bonus }}元</text>
                </view>
                <view class="detail-item">
                  <text class="detail-label">其他补贴：</text>
                  <text class="detail-value">{{ currentDetail.salaryDetails.allowance }}元</text>
                </view>
              </view>
            </view>
          </view>
        
          <view class="detail-section">
            <text class="section-title">扣款项</text>
            <view 
              v-for="(deduct, dIndex) in currentDetail.salaryDetails.deductions" 
              :key="dIndex" 
              class="detail-item deduct-item"
            >
              <text class="detail-label">{{ deduct.reason }}：</text>
              <text class="deduct-value">-{{ deduct.amount }}元</text>
            </view>
            <view class="detail-item total">
              <text class="detail-label">扣款合计：</text>
              <text class="deduct-total">-{{ totalDeductions }}元</text>
            </view>
          </view>
        
          <view class="detail-section">
            <text class="section-title">工资汇总</text>
            <view class="summary-item">
              <text class="summary-label">税前应发：</text>
              <text class="summary-value">{{ currentDetail.preTaxSalary }}元</text>
            </view>
            <view class="summary-item">
              <text class="summary-label">个税扣除：</text>
              <text class="summary-tax">-{{ currentDetail.taxAmount }}元</text>
            </view>
            <view class="summary-item total">
              <text class="summary-label">实发金额：</text>
              <text class="summary-total">{{ currentDetail.postTaxSalary }}元</text>
            </view>
          </view>
        
          <view class="detail-item">
            <button @tap="editSalary(currentDetail, filteredEmployees.findIndex(e => e.name === currentDetail.name))" 
              class="edit-btn">
              编辑工资
            </button>
          </view>
        </view>
      </uni-popup-dialog>
    </uni-popup>
	
	<!-- 新增编辑工资弹窗 -->
	  <uni-popup ref="editSalaryPopup" type="dialog">
	    <uni-popup-dialog 
	      title="编辑工资明细" 
	      mode="base" 
	      @confirm="saveSalaryEdit"
	      @close="cancelEdit"
	    >
	      <view class="edit-container">
	        <view class="form-section">
	          <view class="form-item">
	            <text class="form-label">基本工资：</text>
	            <input type="number" v-model="editData.baseSalary" class="form-input"/>
	          </view>
	          <view class="form-item">
	            <text class="form-label">绩效奖金：</text>
	            <input type="number" v-model="editData.bonus" class="form-input"/>
	          </view>
	          <view class="form-item">
	            <text class="form-label">其他补贴：</text>
	            <input type="number" v-model="editData.allowance" class="form-input"/>
	          </view>
	        </view>
	
	        <view class="deduct-section">
	          <text class="section-title">扣款项管理</text>
	          <view 
	            v-for="(deduct, index) in editData.deductions" 
	            :key="index" 
	            class="deduct-item"
	          >
	            <input 
	              v-model="deduct.reason" 
	              placeholder="扣款原因" 
	              class="deduct-input"
	            />
	            <input 
	              type="number" 
	              v-model="deduct.amount" 
	              placeholder="金额" 
	              class="deduct-input amount"
	            />
	            <button @tap="removeDeduct(index)" class="deduct-btn">删除</button>
	          </view>
	          <button @tap="addDeduct" class="add-deduct">+ 添加扣款项</button>
	        </view>
	      </view>
	    </uni-popup-dialog>
	  </uni-popup>
  </view>
</template>

<script>
export default {
  data() {
    return {
	// 新增编辑相关数据
      editData: {
        baseSalary: 0,
        bonus: 0,
        allowance: 0,
        deductions: []
      },
      currentEditIndex: -1,
      searchName: '',
      employees: [
        {
          name: '李文文',
          department: '生产部',
          status: '在职',
          preTaxSalary: 8500,
          taxAmount: 450,
          postTaxSalary: 8050,
          paid: false,
          salaryDetails: {
            baseSalary: 8000,
            bonus: 1000,
            allowance: 500,
            deductions: [
              { reason: '社保扣除', amount: 350 },
              { reason: '考勤扣款', amount: 200 }
            ]
          }
        },
		{
		  name: '钟文慧',
		  department: '生产部',
		  status: '在职',
		  preTaxSalary: 8500,
		  taxAmount: 450,
		  postTaxSalary: 8050,
		  paid: false,
		  salaryDetails: {
		    baseSalary: 8000,
		    bonus: 1000,
		    allowance: 500,
		    deductions: [
		      { reason: '社保扣除', amount: 350 },
		      { reason: '考勤扣款', amount: 200 }
		    ]
		  }
		},
		{
		  name: '阿肥',
		  department: '生产部',
		  status: '在职',
		  preTaxSalary: 8500,
		  taxAmount: 450,
		  postTaxSalary: 8050,
		  paid: false,
		  salaryDetails: {
		    baseSalary: 8000,
		    bonus: 1000,
		    allowance: 500,
		    deductions: [
		      { reason: '社保扣除', amount: 350 },
		      { reason: '考勤扣款', amount: 200 }
		    ]
		  }
		},
        {
          name: '张慧慧',
          department: '采购部',
          status: '在职',
          preTaxSalary: 12000,
          taxAmount: 980,
          postTaxSalary: 11020,
          paid: true,
          salaryDetails: {
            baseSalary: 10000,
            bonus: 3000,
            allowance: 1000,
            deductions: [
              { reason: '个人所得税', amount: 980 },
              { reason: '公积金', amount: 500 }
            ]
          }
        }
      ],
      currentDetail: {}
    }
  },
  computed: {
    filteredEmployees() {
      return this.employees.filter(item => 
        item.name.toLowerCase().includes(this.searchName.toLowerCase())
		)
    },
    totalDeductions() {
      return this.currentDetail.salaryDetails?.deductions?.reduce((sum, d) => sum + d.amount, 0) || 0
    }
  },
  methods: {
    refreshList() {
      uni.showToast({ title: '列表已刷新', icon: 'success' })
    },
    paySalary(index) {
      uni.showModal({
        title: '确认发放',
        content: `确定要发放${this.employees[index].name}的工资吗？`,
        success: (res) => {
          if (res.confirm) {
            this.employees[index].paid = true
            uni.showToast({ title: '发放成功', icon: 'success' })
          }
        }
      })
    },
    showDetail(item) {
      this.currentDetail = {...item}
      this.$refs.detailPopup.open()
    },
	 // 新增方法
	    editSalary(item, index) {
	      this.currentEditIndex = index
	      this.editData = {
	        baseSalary: item.salaryDetails.baseSalary,
	        bonus: item.salaryDetails.bonus,
	        allowance: item.salaryDetails.allowance,
	        deductions: JSON.parse(JSON.stringify(item.salaryDetails.deductions))
	      }
	      this.$refs.editSalaryPopup.open()
	    },
	    addDeduct() {
	      this.editData.deductions.push({ reason: '', amount: 0 })
	    },
	    removeDeduct(index) {
	      this.editData.deductions.splice(index, 1)
	    },
	    saveSalaryEdit() {
	      // 数据验证
	      if (!this.validateSalaryData()) return
	      
	      const employee = this.employees[this.currentEditIndex]
	      // 更新基础数据
	      employee.salaryDetails.baseSalary = Number(this.editData.baseSalary)
	      employee.salaryDetails.bonus = Number(this.editData.bonus)
	      employee.salaryDetails.allowance = Number(this.editData.allowance)
	      
	      // 处理扣款项
	      employee.salaryDetails.deductions = this.editData.deductions
	        .filter(d => d.reason && d.amount > 0)
	        .map(d => ({
	          reason: d.reason,
	          amount: Number(d.amount)
	        }))
	      
	      // 重新计算总工资
	      this.calculateTotalSalary(employee)
	      
	      uni.showToast({ title: '修改成功', icon: 'success' })
	      this.$refs.editSalaryPopup.close()
	    },
	    validateSalaryData() {
	      if (this.editData.baseSalary < 0) {
	        uni.showToast({ title: '基本工资不能为负', icon: 'none' })
	        return false
	      }
	      if (this.editData.bonus < 0) {
	        uni.showToast({ title: '绩效奖金不能为负', icon: 'none' })
	        return false
	      }
	      if (this.editData.allowance < 0) {
	        uni.showToast({ title: '补贴不能为负', icon: 'none' })
	        return false
	      }
	      return true
	    },
	    calculateTotalSalary(employee) {
	      // 计算税前工资
	      const totalIncome = employee.salaryDetails.baseSalary + 
	                         employee.salaryDetails.bonus + 
	                         employee.salaryDetails.allowance
	      
	      // 计算总扣款
	      const totalDeduct = employee.salaryDetails.deductions.reduce((sum, d) => sum + d.amount, 0)
	      
	      // 更新税前税后工资
	      employee.preTaxSalary = totalIncome - totalDeduct
	      employee.postTaxSalary = employee.preTaxSalary - employee.taxAmount
	    },
	    cancelEdit() {
	      this.currentEditIndex = -1
	      this.editData = {
	        baseSalary: 0,
	        bonus: 0,
	        allowance: 0,
	        deductions: []
	      }
	    }
  }
}
</script>

<style scoped>
	.edit-container {
  padding: 20rpx;
  max-height: 70vh;
  overflow-y: auto;
}

.form-section {
  margin-bottom: 30rpx;
}

.form-item {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.form-label {
  width: 160rpx;
  font-size: 28rpx;
  color: #666;
}

.form-input {
  flex: 1;
  border: 1rpx solid #eee;
  padding: 10rpx;
  border-radius: 6rpx;
}

.deduct-section {
  border-top: 1rpx solid #eee;
  padding-top: 20rpx;
}

/* 编辑按钮优化 */
.edit-btn {
  height: 60rpx;
  line-height: 60rpx;
  font-size: 26rpx;
  margin: 15rpx auto;
  width: 200rpx;
}

.deduct-item {
  display: flex;
  gap: 10rpx;
  margin-bottom: 15rpx;
}
.deduct-input {
  flex: 1;
  border: 1rpx solid #eee;
  padding: 10rpx;
  border-radius: 6rpx;
}

.deduct-input.amount {
  width: 200rpx;
}

.deduct-btn {
  width: 120rpx;
  background: #ff4444;
  color: white;
  font-size: 24rpx;
}

.add-deduct {
  width: 100%;
  background: #f0f0f0;
  color: #666;
  margin-top: 15rpx;
}
.container {
  padding: 20rpx;
  background-color: #f8f8f8;
}

.search-box {
  display: flex;
  gap: 20rpx;
  margin-bottom: 30rpx;
  padding: 0 20rpx;
}

.search-input {
  flex: 1;
  height: 70rpx;
  padding: 0 20rpx;
  background: #fff;
  border-radius: 8rpx;
  font-size: 28rpx;
  border: 1rpx solid #eee;
}

.refresh-btn {
  width: 180rpx;
  height: 70rpx;
  line-height: 70rpx;
  background: #4CAF50;
  color: white;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.list-container {
  height: calc(100vh - 160rpx);
  padding: 0 20rpx;
}

.list-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  margin-bottom: 20rpx;
  background: white;
  border-radius: 12rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);
}

.header {
  display: flex;
  align-items: center;
  margin-bottom: 15rpx;
}

.name {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
  margin-right: 20rpx;
}

.status {
  font-size: 24rpx;
  padding: 4rpx 12rpx;
  border-radius: 4rpx;
}

.status.active {
  background: #e8f5e9;
  color: #4CAF50;
}

.status.leave {
  background: #ffebee;
  color: #f44336;
}

.details {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.department, .salary {
  font-size: 26rpx;
  color: #666;
}

.separator {
  color: #ddd;
  margin: 0 10rpx;
}

.action-container {
  display: flex;
  flex-direction: column;
  gap: 15rpx;
  margin-left: 20rpx;
}

.action-btn {
  width: 140rpx;
  height: 60rpx;
  line-height: 60rpx;
  font-size: 26rpx;
  border-radius: 8rpx;
}

.pay {
  background: #4CAF50;
  color: white;
}

.detail {
  background: #2196F3;
  color: white;
}

.paid-text {
  color: #888;
  font-size: 24rpx;
  text-align: center;
}

/* 优化后的详情弹窗样式 */
.detail-container {
  padding: 20rpx 30rpx;
  width: 680rpx;
  max-height: 80vh;
  overflow-y: auto;
}

.detail-section {
  margin-bottom: 20rpx;
  padding-bottom: 15rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.section-title {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 15rpx;
  padding-left: 10rpx;
  border-left: 4rpx solid #2196F3;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10rpx;
  padding: 8rpx 0;
  line-height: 1.4;
}

.detail-label {
  color: #666;
  font-size: 26rpx;
  flex-shrink: 0;
  width: 160rpx;
}

.detail-value {
  color: #333;
  font-size: 26rpx;
  font-weight: 500;
  text-align: right;
  flex: 1;
  word-break: break-all;
  padding-left: 20rpx;
}


/* 扣款项优化 */
.deduct-item {
  padding: 6rpx 0;
}

.deduct-value {
  font-size: 24rpx;
}
.deduct-total {
  color: #f44336;
  font-weight: bold;
}

/* 汇总部分优化 */
.summary-item {
  margin: 15rpx 0;
  padding: 8rpx 0;
}

.summary-label {
  font-size: 26rpx;
}

.summary-total {
  font-size: 30rpx;
}

.summary-value {
  color: #333;
  font-weight: 500;
}

.summary-tax {
  color: #f44336;
}


.paid {
  color: #4CAF50;
}

.unpaid {
  color: #f44336;
}

/* 调整分割线颜色 */
.detail-section:last-child {
  border-bottom: none;
}
</style>