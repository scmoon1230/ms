class App {

    #swiper;
    #appData;

    constructor(appData) {
        this.#appData = appData;
    }

    init = (appData) => {
        // IMAGE CI
        let sImgUrl = `${this.#appData.contextRoot}/images/wrks/lgn/v2/logo_login_${this.#appData.dstrtCd}_1.png`;
        let isExists = this.#utils.image.isExists(sImgUrl);
        const $imgCi = $('#img-ci');
        if (isExists) {
            $imgCi.attr('src', sImgUrl);
            $imgCi.css('max-width', 200);
        } else {
            $imgCi.remove();
        }
        // IMAGE BG
        for (let nIndex = 1; nIndex < 10; nIndex++) {
            sImgUrl = `${this.#appData.contextRoot}/images/wrks/lgn/v2/bg_login_${this.#appData.dstrtCd}_${nIndex}.png`;
            isExists = this.#utils.image.isExists(sImgUrl);
            if (isExists) {
                $('ul.swiper-wrapper').append($('<li/>', {
                    'class': 'swiper-slide',
                    'style': `background-image:url('${sImgUrl}');`
                }));
            } else {
                break;
            }
        }

        if (this.#swiper != null) {
            this.#swiper.destroy(true, true);
            this.#swiper = null;
        }

        this.#swiper = new Swiper('#loginSlider .swiper-container', {
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
        this.#swiper.on('slideChange', () => {
            let elLi = this.#swiper.slides[this.#swiper.activeIndex];
            let sUrl = $(elLi).css('background');
            let sReplace = sUrl.replace(/.+url\(['"](.+)['"]\).+/, '$1');
            let oRgb = this.#utils.image.averageRgb(sReplace);
            const nLuma = 0.2126 * oRgb.r + 0.7152 * oRgb.g + 0.0722 * oRgb.b;
            if (nLuma < 127.5) {
                $('#loginBox').css({
                    'background': 'rgba(255, 255, 255, 0.07)',
                });
            } else {
                $('#loginBox').css({
                    'background': 'rgba(0, 0, 0, 0.07)',
                });
            }
        });

        $('.btnLogin').on('click', () => this.#login(appData));

        $('input.login').on('keypress', (event) => {
            if (event.keyCode === 13) this.#login(appData);
        });
    }

    #login = (appData) => {
        const $userId = $('#userId');
        const $userNm = $('#userNm');
        const $moblNo = $('#moblNo');
        if ($userId.val().trim() === '') {
            alert('아이디를 입력하세요.');
            $userId.focus();
            return;
        }

        if ($userNm.val() === '') {
            alert('이름을 입력하세요.');
            $userNm.focus();
            return;
        }

        if ($moblNo.val() === '') {
            alert('모바일번호를 입력하세요.');
            $moblNo.focus();
            return;
        }

        let url = this.#appData.contextRoot + '/wrks/lgn/findpwd.json';
        let params = `userId=${$userId.val()}&userNm=${$userNm.val()}&moblNo=${$moblNo.val()}`;

        $.ajax({
            type: 'POST',
            url: url,
            dataType: 'json',
            data: params,
            success: (data) => {
                alert(data.msg);
            },
            error: (e) => {
                alert(e.responseText);
            }
        });
    }

    #utils = {
        image: {
            isExists: (url) => {
                let http = new XMLHttpRequest();
                http.open('HEAD', url, false);
                http.send();
                return http.status !== 404;
            },
            averageRgb: (url) => {
                let blockSize = 5, // only visit every 5 pixels
                    defaultRGB = {r: 0, g: 0, b: 0}, // for non-supporting envs
                    canvas = document.createElement('canvas'),
                    context = canvas.getContext && canvas.getContext('2d'),
                    data, width, height,
                    i = -4,
                    length,
                    rgb = {r: 0, g: 0, b: 0},
                    count = 0;

                if (!context) return defaultRGB;

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
