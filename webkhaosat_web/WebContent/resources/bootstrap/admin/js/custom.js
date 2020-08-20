function showMessageSuccess() {
	window.location.href="http://localhost:webkhaosat_web/pages/web/ToanBoKS.jsf";
}
window.onbeforeunload = function () {
    return "Bạn có chắc chắn muốn đóng?";
};
