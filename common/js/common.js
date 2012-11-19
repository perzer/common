Array.prototype.unique = function() {
	this.sort();
	for (var i = 1; i < this.length; i++) {
		if (this[i] === this[i - 1]) {
			this.splice(i--, 1);
		}
	}
	return this;
}

String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};

//判断是否是合法的手机号码格式
function isValidePhone(value){
	if (value.length == 11){
		var gvPhoneRegExp = new Array();
		gvPhoneRegExp.push(/^14[57]\d{8}$/);
		gvPhoneRegExp.push(/^15[012356789]\d{8}$/);
		gvPhoneRegExp.push(/^13[0-9]\d{8}$/);
		gvPhoneRegExp.push(/^18[0256789]\d{8}$/);   
		
		var lvCellphoneIsOk = false;
		for (var i=0;i < gvPhoneRegExp.length;i++){
			if(gvPhoneRegExp[i].test(value)){
				lvCellphoneIsOk = true;
				break;
			}
		} 		
		if (lvCellphoneIsOk){
			return true;
		} else {
			return false;
		}
	} else {
		return false;
	}
}