<template>
  <view>
	  <uni-notice-bar show-icon scrollable
	  				text="如有订单消息请及时处理!!!" />
	  <uni-card title="流水">
	    <uni-row>
	      <uni-col :span="6" v-for="(item, index) in controll_items" :key="index" >
	        <text class="col-center_controll">{{ item.num }}</text>
	        <text class="col-center_controll">{{ item.text }}</text>
	      </uni-col>
	    </uni-row>
	  </uni-card>
	  
	  <uni-card title="支付管理">
	    <uni-row>
	      <uni-col :span="6" v-for="(item, index) in items" :key="index">
			  <view class="col-center">
				  <view style="text-align: center;">
				  		<image style="width: 60px; height: 60px;" :src="item.image" :alt="item.text" @click="finance_a(item.text)"></image>
				  </view>
				  <view style="text-align: center;">
						<text>{{ item.text }}</text>
				  </view>
			  </view>
	      </uni-col> 
	    </uni-row>
	  </uni-card>
	  
	  <uni-card title="资金管理">
	    <uni-row>
	      <uni-col :span="6" v-for="(item, index) in in_out_items" :key="index" >
			<view class="col-center">
				  <view style="text-align: center;">
				  		 <image style="width: 60px; height: 60px;" :src="item.image" :alt="item.text" @click="finance_b(item.text)"></image>
				  </view>
				  <view style="text-align: center;">
						<text>{{ item.text }}</text>
				  </view> 
			  </view>			
	      </uni-col>
	    </uni-row>
	  </uni-card>
	  
	  <uni-card title="员工薪酬管理">
	    <uni-row>
	      <uni-col :span="6" v-for="(item, index) in finance_items" :key="index" >
				<view class="col-center">
				  <view style="text-align: center;">
				  		<image style="width: 60px; height: 60px;" :src="item.image" :alt="item.text" @click="finance_c(item.text)"></image>
				  </view>
				  <view style="text-align: center;">
						<text>{{ item.text }}</text>
				  </view>
			  </view>			
	      </uni-col>
	    </uni-row>
	  </uni-card>
  </view>
</template>

<script>
export default {
  data() {
    return {
	  controll_items: [
	    { num: '1622', text: '今日收入' },
	    { num: '782313', text: '今日支出' },
		{ num: '551511', text: '利润' },
	    { num: '0', text: '报损' },
	  ],
      items: [
        { image: '/static/cw_picture/home_ui/支付接口.png', text: '支付接口' },
		{ image: '/static/cw_picture/home_ui/历史订单.png', text: '订单管理' },
		// { image: '/static/cw_picture/home_ui/支付审批.png', text: '支付审批' },
		// { image: '/static/cw_picture/home_ui/凭证管理.png', text: '凭证管理' },
		{ image: '/static/cw_picture/home_ui/发票管理.png', text: '发票凭证' },
		{ image: '/static/cw_picture/home_ui/合同管理.png', text: '合同管理' },
      ],
	  in_out_items: [
	  	{ image: '/static/cw_picture/home_ui/工资管理.png', text: '工资管理' },
	  	{ image: '/static/cw_picture/home_ui/奖金审批.png', text: '奖金审批' },
		{ image: '/static/cw_picture/home_ui/门店管理.png', text: '门店管理' },
	  ],
	  finance_items: [
	  	{ image: '/static/cw_picture/home_ui/报账审批.png', text: '报账审批' },
	  	{ image: '/static/cw_picture/home_ui/流水统计.png', text: '流水统计' },
		{ image: '/static/cw_picture/home_ui/支付审批.png', text: '成本核算' },
	  ],
    };
  },
  methods: {
	  finance_a(menu) {
		  switch(menu) {
			  case '订单管理':
			  uni.navigateTo({
			  	url: '/pages/orders_caiwu/orders_caiwu',
				success: () => {
				    console.log('跳转成功');
				  },
				  fail: (err) => {
				    console.log('跳转失败', err);
				  }
			  })
			  break;
		  }
		 
		  switch(menu) {
			  case '发票凭证':
			  uni.navigateTo({
			  	url: '/pages/invoice/invoice',
				success: () => {
				    console.log('跳转成功');
				  },
				  fail: (err) => {
				    console.log('跳转失败', err);
				  }
			  })
			  break;
		  }
		  switch(menu) {
		  			  case '合同管理':
		  			  uni.navigateTo({
		  			  	url: '/pages/contract_caiwu/contract_caiwu',
		  				success: () => {
		  				    console.log('跳转成功');
		  				  },
		  				  fail: (err) => {
		  				    console.log('跳转失败', err);
		  				  }
		  			  })
		  			  break;
		  }
	  },
	  finance_b(menu) {
	  		  switch(menu) {
	  			  case '工资管理':
	  			  uni.navigateTo({
	  			  	url: '/pages/salary_caiwu/salary_caiwu',
	  				success: () => {
	  				    console.log('跳转成功');
	  				  },
	  				  fail: (err) => {
	  				    console.log('跳转失败', err);
	  				  }
	  			  })
	  			  break;
	  		  }
	  		  switch(menu) {
	  			  case '奖金审批':
	  			  uni.navigateTo({
	  			  	url: '/pages/bonus/bonus',
	  				success: () => {
	  				    console.log('跳转成功');
	  				  },
	  				  fail: (err) => {
	  				    console.log('跳转失败', err);
	  				  }
	  			  })
	  			  break;
	  		  }
			 switch(menu) {
		  			  case '门店管理':
		  			  uni.navigateTo({
		  			  	url: '/pages/store_manage/store_manage',
		  				success: () => {
		  				    console.log('跳转成功');
		  				  },
		  				  fail: (err) => {
		  				    console.log('跳转失败', err);
		  				  }
		  			  })
		  		  break;
	}	
	  },
	  finance_c(menu) {
	  		  switch(menu) {
	  			  case '报账审批':
	  			  uni.navigateTo({
	  			  	url: '/pages/account_caiwu/account_caiwu',
	  				success: () => {
	  				    console.log('跳转成功');
	  				  },
	  				  fail: (err) => {
	  				    console.log('跳转失败', err);
	  				  }
	  			  })
	  			  break;
	  		  }
			  switch(menu) {
				  case '流水统计':
	  			  uni.navigateTo({
	  			  	url: '/pages/flow_caiwu/flow_caiwu',
	  				success: () => {
	  				    console.log('跳转成功');
	  				  },
	  				  fail: (err) => {
	  				    console.log('跳转失败', err);
	  				  }
	  			  })
	  			  break;
			  }
			  switch(menu) {
				  case '成本核算':
	  			  uni.navigateTo({
	  			  	url: '/pages/cost_caiwu/cost_caiwu',
	  				success: () => {
	  				    console.log('跳转成功');
	  				  },
	  				  fail: (err) => {
	  				    console.log('跳转失败', err);
	  				  }
	  			  })
	  			  break;
			  }			  
	  },
	  
  }
};
</script>

<style>
/* 让每个栅格内容居中 */
.col-center {
  display: flex;
  flex-direction: column; /* 垂直排列 */
  align-items: center; /* 水平居中 */
  justify-content: center; /* 垂直居中 */
}

.col-center_controll {
  display: flex;
  flex-direction: column; /* 垂直排列 */
  align-items: center; /* 水平居中 */
  justify-content: center; /* 垂直居中 */
  color: blueviolet;
}

/* 文字与图片的间距 */
.text-below-image {
  margin-top: 8px; /* 调整间距 */
  text-align: center; /* 文字水平居中 */
}
</style>