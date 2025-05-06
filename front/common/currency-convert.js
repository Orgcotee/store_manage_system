// common/currency-convert.js

/**
 * 数字金额转中文大写
 * @param {Number} n 金额数值
 * @returns {String} 中文大写金额
 */
export default function convertCurrency(n) {
  if (!/^(0|[1-9]\d*)(\.\d+)?$/.test(n)) {
    return "金额格式错误";
  }

  let unit = "千百拾亿千百拾万千百拾元角分";
  let str = "";
  n += "00";
  let p = n.indexOf('.');
  if (p >= 0) {
    n = n.substring(0, p) + n.substr(p+1, 2);
  }
  unit = unit.substr(unit.length - n.length);
  
  for (let i = 0; i < n.length; i++) {
    str += "零壹贰叁肆伍陆柒捌玖".charAt(n.charAt(i)) + unit.charAt(i);
  }

  return str
    .replace(/零(千|百|拾|角)/g, "零")
    .replace(/(零)+/g, "零")
    .replace(/零(万|亿|元)/g, "$1")
    .replace(/(亿)万|壹(拾)/g, "$1$2")
    .replace(/^元零?|零分/g, "")
    .replace(/元$/g, "元整")
    .replace(/角$/g, "角整");
}

// 示例：convertCurrency(1234.56) => "壹仟贰佰叁拾肆元伍角陆分"