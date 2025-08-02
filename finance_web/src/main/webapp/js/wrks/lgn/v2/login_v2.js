const App = function () {
	let swiper = null;

	this.init = function (appData) {
		// IMAGE CI
		//let sImgUrl = appData.contextRoot + '/images/wrks/lgn/v2/logo_login_' + appData.dstrtCd + '_1.png';
		let sImgUrl = appData.contextRoot + '/images/wrks/lgn/v2/logo_login_10000_11.png';
		let isExists = utils.image.isExists(sImgUrl);
		if (isExists) {
			$('#img-ci').attr('src', sImgUrl);
			$('#img-ci').css('max-width', 200);
		} else {
			$('#img-ci').remove();
		}
		// IMAGE BG
		for (let nIndex = 1; nIndex < 10; nIndex++) {
			sImgUrl = appData.contextRoot + '/images/wrks/lgn/v2/bg_login_' + appData.dstrtCd + '_' + nIndex + '.png';
			isExists = utils.image.isExists(sImgUrl);
			if (isExists) {
				$('ul.swiper-wrapper').append($('<li/>', {
					'class': 'swiper-slide',
					'style': 'background-image:url("' + sImgUrl + '");'
				}));
			} else {
				break;
			}
		}

		if (swiper != null) {
			swiper.destroy(true, true);
			swiper = null;
		}

		swiper = new Swiper('#loginSlider .swiper-container', {
			effect: 'fade',
			speed: 1000,
			loop: true,
			autoplay: {
				delay: 10000,
			},
			allowTouchMove: false, //배경 터치 슬라이드 막는 옵션
			on: {
				slideChangeTransitionStart: () => {
				},
			}
		});

		// 배경 밝기에 따라 로그인 박스 색상 변경
		// nLuma가 0에 가까울 수록 배경은 어둡고 255에 가까울수록 배경은 밝은 색
		swiper.on('slideChange', function () {
			let elLi = swiper.slides[swiper.activeIndex];
			let sUrl = $(elLi).css('background');
			let sReplace = sUrl.replace(/.+url\(['"](.+)['"]\).+/, '$1');
			let oRgb = utils.image.averageRgb(sReplace);
			const nLuma = 0.2126 * oRgb.r + 0.7152 * oRgb.g + 0.0722 * oRgb.b;
			if (nLuma < 127.5) {	// 배경이 어두울 때
				$('#loginBox').css({
					'background': 'rgba(255, 255, 255, 0.07)',
				});
			} else {				// 배경이 밝을 때
				$('#loginBox').css({
					'background': 'rgba(0, 0, 0, 0.27)',
				});
			}
		});

		$('.btnLogin').on('click', () => {
			login(appData);
		});

		$('input.login').on('keypress', (event) => {
			if (event.keyCode == 13) login(appData);
		});
	}

	const login = function (appData) {
		
		//alert(window.location.protocol);
		let ssl = 'N';
		if ( window.location.protocol.indexOf('https') != -1 ) {
			ssl = 'Y';
		}
		//alert('ssl = '+ssl);
	
		if ($('#id').val().trim() == '')	{	alert('아이디를 입력해 주세요.');	$('#id').focus();	return;	}
		if ($('#pw').val() == '')			{	alert('비밀번호를 입력해 주세요.');	$('#pw').focus();	return;	}

		let url = appData.contextRoot + '/wrks/lgn/login.json';
		let params = 'userId=' + $('#id').val() + '&pwd=' + $('#pw').val() + '&ssl=' + ssl;

		$.ajax({
			type: 'POST',
			url: url,
			dataType: 'json',
			data: params,
			success: function (data) {
				if (data.ret == 1) {
					location.href = appData.contextRoot + '/' + data.redirect;
					/*
					let sLoginDplctnYn = data.loginDplctnYn || 'Y';
					if (sLoginDplctnYn == 'N') {
						let protocol = "ws";
						if ("Y"==ssl) protocol = "wss";
						let socket = new WebSocket(protocol + '://' + window.location.host + '/ws/evt.do?page=login');

						socket.onmessage = (event) => {
							const oData = JSON.parse(event.data);
							if (oData.session) {
								location.href = appData.contextRoot + '/' + data.redirect;
							} else {
								let isConfirmed = confirm('이미 로그인 되어 있습니다. 기존 로그인을 강제 종료하시겠습니까?');
								if (isConfirmed) {
									socket.send('LOGOUT');
									location.href = appData.contextRoot + '/' + data.redirect;
								} else {
									location.href = appData.contextRoot + '/wrks/lgn/logout.do';
								}
							}
						};
					} else {
						location.href = appData.contextRoot + '/' + data.redirect;
					}
					*/
				} else {
					alert(data.msg);
				}
			},
			error: function (e) {
				alert('접속이 지연되고 있습니다. 잠시후 시도해주십시오');
			}
		});
	}

	const utils = {
		image: {
			isExists: function (url) {
				let http = new XMLHttpRequest();
				http.open('HEAD', url, false);
				http.send();
				return http.status != 404;
			},
			averageRgb: function (url) {
				let blockSize = 5, // only visit every 5 pixels
					defaultRGB = {r: 0, g: 0, b: 0}, // for non-supporting envs
					canvas = document.createElement('canvas'),
					context = canvas.getContext && canvas.getContext('2d'),
					data, width, height,
					i = -4,
					length,
					rgb = {r: 0, g: 0, b: 0},
					count = 0;

				if (!context) {
					return defaultRGB;
				}

				let elImage = document.createElement('img');
				elImage.src = url;
				height = canvas.height = elImage.naturalHeight || elImage.offsetHeight || elImage.height;
				width = canvas.width = elImage.naturalWidth || elImage.offsetWidth || elImage.width;
				context.drawImage(elImage, 0, 0);

				try {
					data = context.getImageData(0, 0, width, height);
				} catch (e) {
					return defaultRGB;
				}

				length = data.data.length;

				while ((i += blockSize * 4) < length) {
					++count;
					rgb.r += data.data[i];
					rgb.g += data.data[i + 1];
					rgb.b += data.data[i + 2];
				}

				// ~~ used to floor values
				rgb.r = ~~(rgb.r / count);
				rgb.g = ~~(rgb.g / count);
				rgb.b = ~~(rgb.b / count);
				return rgb;
			}
		},
	}
}
