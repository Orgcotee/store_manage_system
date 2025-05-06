<template>
  <view>
	  <uni-notice-bar show-icon scrollable
	  				text="部门经理,欢迎您!" />
	  
	  <uni-card title="日常办公">
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
	
  </view>
</template>

<script>
export default {
  data() {
    return {
      items: [
        { image: '/static/cw_picture/home2_ui/报账.png', text: '报账' },
		{ image: '/static/cw_picture/home2_ui/奖金.png', text: '奖金申请' },
		{ image: '/static/cw_picture/home2_ui/员工.png', text: '员工管理' },
      ],
    };
  },
  methods: {
	  finance_a(menu) {
		  switch(menu) {
			  case '报账':
			  uni.navigateTo({
			  	url: '/pages/account_apply/account_apply',
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
			  case '奖金申请':
			  uni.navigateTo({
			  	url: '/pages/bonus_apply/bonus_apply',
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
		  			  case '员工管理':
		  			  uni.navigateTo({
		  			  	url: '/pages/staff_manage/staff_manage',
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