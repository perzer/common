var aNum = new Array(10);
aNum[0] = "%u96F6"; // 零
aNum[1] = "%u58F9"; // 壹
aNum[2] = "%u8d30"; // 贰
aNum[3] = "%u53c1"; // 叁
aNum[4] = "%u8086"; // 肆
aNum[5] = "%u4F0D"; // 伍
aNum[6] = "%u9646"; // 陆
aNum[7] = "%u67D2"; // 柒
aNum[8] = "%u634C"; // 捌
aNum[9] = "%u7396"; // 玖

var HUNDREDMILLION = 0
var TENTHOUSAND = 1;
var THOUSAND = 2;
var HUNDRED = 3;
var TEN = 4;
var YUAN = 5;
var JIAO = 6;
var CENT = 7;
var ZHENG = 8;
var aUnit = new Array(9);
aUnit[HUNDREDMILLION] = "%u4EBF"; // 亿
aUnit[TENTHOUSAND] = "%u4E07"; // 万
aUnit[THOUSAND] = "%u4EDF"; // 仟
aUnit[HUNDRED] = "%u4F70"; // 佰
aUnit[TEN] = "%u62FE"; // 拾
aUnit[YUAN] = "%u5143"; // 元
aUnit[JIAO] = "%u89D2"; // 角
aUnit[CENT] = "%u5206"; // 分
aUnit[ZHENG] = "%u6574"; // 整
function toChineseCash(cash) {
	var integerCash = "";
	var decimalCash = "";
	var integerCNCash = "";
	var decimalCNCash = ""
	var dotIndex = 0;
	var cnCash = "";
	var Cash = "";

	Cash = trimStr(cash);
	if (!Cash) {
		return cnCash;
	}

	if (!checkFloat(Cash)) {
		return cnCash;
	}

	dotIndex = Cash.indexOf('.');
	if (dotIndex != -1) {
		integerCash = Cash.substring(0, dotIndex);
		decimalCash = Cash.substring(dotIndex + 1);
	} else {
		integerCash = Cash;
		decimalCash = null;
	}

	integerCNCash = filterCharacter(integerCash, '0');
	if (integerCNCash == null)
		integerCNCash = "";
	else
		integerCNCash = convertIntegerToChineseCash(integerCNCash);

	decimalCNCash = convertDecimalToChineseCash(decimalCash, false);

	if (decimalCNCash == null || decimalCNCash.length == 0) {
		if (integerCNCash == null || integerCNCash.length == 0)
			cnCash = aNum[0] + aUnit[YUAN] + aUnit[ZHENG]; // "零元整"
		else
			cnCash = integerCNCash + aUnit[YUAN] + aUnit[ZHENG]; // "元整"
	} else {
		if (integerCNCash == null || integerCNCash.length == 0)
			cnCash = decimalCNCash;
		else
			cnCash = integerCNCash + aUnit[YUAN] + decimalCNCash; // "元"
	}
	return unescape(cnCash);
}
// 判断数值,是否为浮点数
function checkFloat(string) {
	var length1, i, j;
	var string1 = "";

	var ofstr = getoff_Qfw(string);
	var oflen = ofstr.length;
	if (oflen > 0 && ofstr.charAt(oflen - 1) == " ")
		return (false);

	string1 = trimStr(string)
	length1 = string1.length;
	if (length1 == 0) {
		alert("错误！空串！");
		return (false);
	}

	j = 0;
	for (i = 0; i < length1; i++) { // 判断每位数字
		if (isNaN(parseInt(string.charAt(i), 10))) {
			if (string.charAt(i) != ".") {
				alert("错误！请输入数值型数据！");
				return (false);
			} else {
				j++;
				if (length1 - i > 3) {
					alert("小数点后只能有两位！");
					return (false);
				}
			}
		}
	}
	if (j > 1) {
		alert("错误！小数点只能有一个!");
		return (false);
	}

	return (true);
}

function filterCharacter(filterString, filterChar) {
	if (filterString == null || filterString.length == 0) {
		return null;
	}

	var i = 0;
	for (; i < filterString.length; i++) {
		if (filterString.charAt(i) != filterChar)
			break;
	}

	var ret = filterString.substring(i, filterString.length);
	ret = (ret.length > 0) ? ret : null;

	return ret;
}
function convertIntegerToChineseCash(cash) {
	var tempCash = "";
	var returnCash = "";

	if (cash == null || cash.length == 0)
		return null;

	var totalLen = cash.length;
	var times = ((cash.length % 4) > 0)
			? (Math.floor(cash.length / 4) + 1)
			: Math.floor(cash.length / 4);
	var remainder = cash.length % 4;
	var i = 0;
	for (; i < times; i++) {
		if (i == 0 && (remainder > 0)) {
			tempCash = cash.substring(0, remainder);
		} else {
			if (remainder > 0)
				tempCash = cash.substring(remainder + (i - 1) * 4, remainder
								+ i * 4);
			else
				tempCash = cash.substring(i * 4, i * 4 + 4);
		}

		tempCash = convert4ToChinese(tempCash, false);
		returnCash += tempCash;
		if (tempCash != null && tempCash.length != 0)
			returnCash += getUnit(times - i);
	}

	return returnCash;
}
function convertDecimalToChineseCash(cash, bOmitBeginZero) {
	var i = 0;
	var bBeginZero = false;
	var bMetZero = false;
	var returnCash = "";

	if (cash == null || cash.length == 0)
		return returnCash;

	for (; i < cash.length; i++) {
		if (i >= 2)
			break;
		if (i == 0 && bOmitBeginZero && cash.charAt(i) == '0') {
			bBeginZero = true;
			continue;
		}
		if (bBeginZero && cash.charAt(i) == '0')
			continue;

		if (cash.charAt(i) != '0') {
			// if( bMetZero )
			// returnCash += aNum[0]; //"零"
			bMetZero = false;
			returnCash += convert(cash.charAt(i));
			switch (i) {
				case 0 :
					returnCash += aUnit[JIAO]; // "角"
					break;
				case 1 :
					returnCash += aUnit[CENT]; // "分"
					break;
				default :
					break;
			}
		} else {
			bMetZero = true;
		}
	}

	return returnCash;
}
function convert4ToChinese(cash, bOmitBeginZero) {
	var i = 0;
	var length = cash.length;
	var bBeginZero = false;
	var bMetZero = false;
	var returnCash = "";

	for (; i < length; i++) {
		if (i == 0 && bOmitBeginZero && cash.charAt(i) == '0') {
			bBeginZero = true;
			continue;
		}
		if (bBeginZero && cash.charAt(i) == '0')
			continue;

		if (cash.charAt(i) != '0') {
			if (bMetZero)
				returnCash += aNum[0]; // "零"
			bMetZero = false;
			returnCash += convert(cash.charAt(i));
			switch (i + (4 - length)) {
				case 0 :
					returnCash += aUnit[THOUSAND]; // "千"
					break;
				case 1 :
					returnCash += aUnit[HUNDRED]; // "佰"
					break;
				case 2 :
					returnCash += aUnit[TEN]; // "拾"
					break;
				case 3 :
					returnCash += "";
					break;
				default :
					break;
			}
		} else {
			bMetZero = true;
		}
	}

	return returnCash;
}
function convert(num) {
	return aNum[parseInt(num)];
}
function getUnit(part) {
	var returnUnit = "";
	var i = 0;

	switch (part) {
		case 1 :
			returnUnit = "";
			break;
		case 2 :
			returnUnit = aUnit[TENTHOUSAND]; // "万"
			break;
		case 3 :
			returnUnit = aUnit[HUNDREDMILLION]; // "亿"
			break;
		default :
			if (part > 3) {
				for (; i < part - 3; i++) {
					returnUnit += aUnit[TENTHOUSAND]; // "万"
				}
				returnUnit += aUnit[HUNDREDMILLION]; // "亿"
			}

			break;
	}

	return returnUnit;
}
function formatMoney(money, point) {
	if (!point) {
		point = 2;
	}
	money = Number(money);
	if (String(money).indexOf(",") > 0) {
		money = String(money).replace(/,/g, "");
		money = Number(money);
	}
	money = String(money.toFixed(point));
	var re = /(-?\d+)(\d{3})/;
	while (re.test(money))
		money = money.replace(re, "$1,$2")
	return money;
}
// 去掉 ","
function getoff_Qfw(cash) {
	var len = cash.length;
	var ch = "";
	var newCash = "";
	for (var ii = 0; ii < len; ii++) {
		ch = cash.charAt(ii);
		if (ch != ",") {
			newCash = newCash + ch;
		}
	}
	return newCash;
}

// TODO del
function trimStr(str) {
	var re = /\s*(\S[^\0]*\S)\s*/;
	re.exec(str);
	return RegExp.$1;
}
