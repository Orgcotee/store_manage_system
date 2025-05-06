// 添加token，拦截错误，添加请求头
import {baseURL,timeout} from '../config/index'
 
const request = function(option){
	// 获取用户传入的url
	var url = option.url;
	// 定义一个正则表达式 必须http 或者https开头
	var reg = /^https?/i;
	// 如果不是http开头
	if(!reg.test(url)){
		// 地址加baseURl
		url=baseURL+url;
	}
	// 添加提请求头
	var  header = option.header||{}
	// 添加token
	header.token = uni.getStorageSync('token');
	// header.Cookie = 'passToken='+uni.getStorageSync('token');
	header.source=1;
	header.channel="h5";
	
	// 加载提示
	var loading = option.loading;
	// 如果有loading就显示loading
	if(loading){
		uni.showLoading(loading)
	}
// 返回一个promise
	return new Promise((resolve,reject)=>{
		// 发起一个request请求
		uni.request({
			url, //请求url
			method:option.method||"GET", //请求方法
			header, //请求头
			timeout,
			data:option.data||option.params, //请求数据
			success(res){
				// 成功返回结果
				if(res.statusCode===200){
					resolve(res.data)
					// 如果是101 没有权限
					// if(res.data.code==101){
					// 	uni.showToast({
					// 		title: res.data.msg,
					// 		icon:'none'
					// 	})
					// 	uni.redirectTo({
					// 		url: '/pages/login/login',
					// 	})
					// }
					if(res.data.code!=200&&res.data.code!=0){
						uni.showToast({
							icon:'none',
							title:res.data.msg||'请求错误'
						})
					}
				}
				

			},
			fail(err){
				// 失败返回失败结果
				uni.showToast({
					title: '请求失败',
					icon:'error'
				})
				console.error(err);
				reject(err)
			},
			complete(){
				// 完成 关闭loading
				if(loading){
					uni.hideLoading()
				}
			}
		})
	})
}
// 定义get简洁方法
request.get=function(url,config){
	return  request({
		url,
		method:"GET",
		...config
	})
}
// 定义post简洁方法
request.post=function(url,data,config){
	return  request({
		url,
		method:"POST",		
		...config,
		data
	})
}
// 导入请求
export default request;