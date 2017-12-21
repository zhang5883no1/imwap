 //mui初始化
       mui.init({
           swipeBack: true //启用右滑关闭功能
       });
       var info = document.getElementById("info");
       document.getElementById("alertBtn").addEventListener('tap', function() {
           mui.alert('请联系您的客服升级权限', '您的直播间听课时间为零', function() {
               info.innerText = '';
           });
       });
       document.getElementById("confirmBtn").addEventListener('tap', function() {
           var btnArray = ['是', '否'];
           mui.confirm('', 'Hello MUI', btnArray, function(e) {
               if (e.index == 0) {
                   info.innerText = '';
               } else {
                   info.innerText = ''
               }
           })
       });
       document.getElementById("promptBtn").addEventListener('tap', function(e) {
           e.detail.gesture.preventDefault(); //修复iOS 8.x平台存在的bug，使用plus.nativeUI.prompt会造成输入法闪一下又没了
           var btnArray = ['确定', '取消'];
           mui.prompt('', '', '', btnArray, function(e) {
               if (e.index == 0) {
                   info.innerText = '' + e.value;
               } else {
                   info.innerText = '';
               }
           })
       });
       document.getElementById("toastBtn").addEventListener('tap', function() {
           mui.toast('');

        mui.init({
            swipeBack: true //启用右滑关闭功能
        });
        (function ($) {
            $('#scroll').scroll({
                indicators: true //是否显示滚动条
            });
            var segmentedControl = document.getElementById('segmentedControl');
            $('.mui-input-group').on('change', 'input', function () {
                if (this.checked) {
                    var styleEl = document.querySelector('input[name="style"]:checked');
                    var colorEl = document.querySelector('input[name="color"]:checked');
                    if (styleEl && colorEl) {
                        var style = styleEl.value;
                        var color = colorEl.value;
                        segmentedControl.className = 'mui-segmented-control' + (style ? (
                                ' mui-segmented-control-' + style) : '') + ' mui-segmented-control-' +
                            color;
                    }
                }
            });
        })(mui);