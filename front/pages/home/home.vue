<template>
  <view>
	  <uni-notice-bar show-icon scrollable
	  				text="库存充足!!如有门店消息请及时处理!!!" />
	  <uni-card title="控制板">
	    <uni-row>
	      <uni-col :span="6" v-for="(item, index) in controll_items" :key="index" >
	        <text class="col-center_controll">{{ item.num }}</text>
	        <text class="col-center_controll">{{ item.text }}</text>
	      </uni-col>
	    </uni-row>
	  </uni-card>
	  
	  <uni-card title="产品管理">
	    <uni-row>
	      <uni-col :span="6" v-for="(item, index) in items" :key="index" >
	        <image class="col-center" style="width: 80px; height: 80px;" :src="item.image" :alt="item.text" @click="product(item.text)"></image>
	        <text class="col-center">{{ item.text }}</text>
	      </uni-col>
	    </uni-row>
	  </uni-card>
	  
	  <uni-card title="进销存">
	    <uni-row>
	      <uni-col :span="6" v-for="(item, index) in in_out_items" :key="index" >
	        <image class="col-center" style="width: 80px; height: 80px;" :src="item.image" :alt="item.text"></image>
	        <text class="col-center">{{ item.text }}</text>
	      </uni-col>
	    </uni-row>
	  </uni-card>
	  
	  <uni-card title="生产">
	    <uni-row>
	      <uni-col :span="6" v-for="(item, index) in produce_items" :key="index" >
	        <image class="col-center" style="width: 80px; height: 80px;" :src="item.image" :alt="item.text"></image>
	        <text class="col-center">{{ item.text }}</text>
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
	    { num: '1622', text: '原材料库存' },
	    { num: '782313', text: '产品库存' },
		{ num: '551511', text: '营业额' },
	    { num: '0', text: '羊毛' },
	  ],
      items: [
        { image: '/static/home/store.png', text: '店家产品' },
		{ image: '/static/home/row.png', text: '原材料产品' },
		{ image: '/static/home/bom.png', text: '原材料码表' },
        { image: '/static/home/search.png', text: '查询' },
		{ image: '/static/home/statistic.png', text: '统计' },
      ],
	  in_out_items: [
	  	{ image: '/static/home/in.png', text: '原材料入库' },
	  	{ image: '/static/home/out.png', text: '成品出库' },
		{ image: '/static/home/purchase.png', text: '采购' },
		{ image: '/static/home/pay.png', text: '账单' },
	  ],
	  produce_items: [
	  	{ image: '/static/home/produce.png', text: '生产计划下达' },
	  	{ image: '/static/home/produce_m.png', text: '生产审查' },
	  ],
    };
  },
  methods: {
	  product(menu) {
		  switch(menu) {
			  case '原材料码表':
			  uni.navigateTo({
			  	url: '/pages/home/bom/bom',
				success: () => {
				    console.log('跳转成功');
				  },
				  fail: (err) => {
				    console.log('跳转失败', err);
				  }
			  })
			  break;
		  }
	  }
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