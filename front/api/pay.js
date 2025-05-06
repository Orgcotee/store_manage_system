import request from '@/utils/request.js'
// 创建订单获取支付信息
export  function createOrder(data){
	return request.post("https://97fca9f2-41f6-449f-a35e-3f135d4c3875.bspapp.com/http/pay",data)
}
// 获取openid
export function getOpenId(data){
	return request.post("https://97fca9f2-41f6-449f-a35e-3f135d4c3875.bspapp.com/http/user-center",data)
}
// 获取小程序openid
export function getMPid(params){
	return request.get("https://ai.qikuedu.com/study_miniprog_openid",{params})
}
// 创建小程序支付订单
export function createMPorder(params){
	return request.get("https://ai.qikuedu.com/study_miniprog_pay",{params})
}