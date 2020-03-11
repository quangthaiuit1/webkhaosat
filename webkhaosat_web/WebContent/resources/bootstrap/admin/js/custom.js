function showMessageSuccess() {
	window.location.href="http://192.168.0.132:8380/webkhaosat_web/pages/web/ToanBoKS.jsf";
}
window.onbeforeunload = function () {
    return "Bạn có chắc chắn muốn đóng?";
};
