// ==UserScript==
// @name           delQDLimit
// @namespace      perzer@163.com
// @include        http://www.qidian.com/BookReader/*.aspx
// @include	   http://vipreader.qidian.com/BookReader/*.aspx
// ==/UserScript==

//显示右键菜单
window.document.oncontextmenu = function(){ return true;}
//去除不能选中
document.onselectstart = true;
document.getElementById('bigcontbox').onmousedown = true;