class CurrentEvent {
	//#evtId
	//#evtOcrNo
	#dstrtCd
	#viewRqstNo
	#evtOcrYmdHms
	//#dtctnYmdhms
	#pointX
	#pointY
	#from
	#to
	#mode
	#tempEvtOcrNo
    #inheritSearchDatetimeYn

	constructor() {
		this.clear();
	}

	clear() {
		//this.#evtId = '';
		//this.#evtOcrNo = '';
		this.#dstrtCd = '';
		this.#viewRqstNo = '';
		this.#evtOcrYmdHms = '';
		//this.#dtctnYmdhms = '';
		this.#pointX = 0;
		this.#pointY = 0;
		this.#from = 0;
		this.#to = 0;
		this.#mode = 'PLAY';
		this.#tempEvtOcrNo = '';
        this.#inheritSearchDatetimeYn = 'N';
	}

	set event(data) {
		//console.log('-- CurrentEvent set event: %o', data);
		//this.#evtId = data.evtId || '';
		//this.#evtOcrNo = data.evtOcrNo || '';
		this.#dstrtCd = data.dstrtCd || '';
		this.#viewRqstNo = data.viewRqstNo || '';
		this.#evtOcrYmdHms = data.evtOcrYmdHms || '';
		//this.#dtctnYmdhms = data.dtctnYmdhms || '';
		this.#pointX = data.pointX || 0;
		this.#pointY = data.pointY || 0;
        this.#inheritSearchDatetimeYn = 'N';
        console.log('-- CurrentEvent set event: %o', this.event);
	}

	get event() {
		return {
			//evtId: this.#evtId,
			//evtOcrNo: this.#evtOcrNo,
			dstrtCd: this.#dstrtCd,
			viewRqstNo: this.#viewRqstNo,
			evtOcrYmdHms: this.#evtOcrYmdHms,
			//dtctnYmdhms: this.#dtctnYmdhms,
			pointX: this.#pointX,
			pointY: this.#pointY,
		}
	}

    set inheritSearchDatetimeYn(inheritSearchDatetimeYn) {
        this.#inheritSearchDatetimeYn = inheritSearchDatetimeYn;
    }

    get inheritSearchDatetimeYn() {
        return this.#inheritSearchDatetimeYn;
    }

	set temp(temp) {
		this.#tempEvtOcrNo = temp.evtOcrNo;
	}

	get temp() {
		return {
			evtOcrNo: this.#tempEvtOcrNo
		}
	}

	get cctv() {
		const $chkCctvSearchYn = $('#chk-cctv-search-yn');
		if ($chkCctvSearchYn.length) {
			let isChecked = $chkCctvSearchYn.is(':checked');
			if (isChecked) {
				this.#mode = 'SEARCH';
				let momentFrom, momentTo, momentNow = moment();
                //const sFormatter = 'YYYY-MM-DD HH:mm:ss';
                const sFormatter = 'YYYY-MM-DD HH:mm';
                const sFormatterForParsing = 'YYYYMMDDHHmmss';
                let isLfpEvent = false;

                if (this.#inheritSearchDatetimeYn === 'Y') {
                    momentFrom = moment($('#params-video-search-ymdhms-fr').val(), sFormatter);
                    momentTo = moment($('#params-video-search-ymdhms-to').val(), sFormatter);
                } else if (this.#evtOcrYmdHms === '') {
					momentFrom = momentNow.clone().subtract(oVms.playbacktimeBase, 'minutes');
					momentTo = momentNow;
				} else {
					//let isLfpEvent = (this.#evtId.startsWith('LFP') && this.#dtctnYmdhms != '');
					//if (isLfpEvent) {
					//	momentFrom = moment(this.#dtctnYmdhms, sFormatterForParsing);
					//} else {
						momentFrom = moment(this.#evtOcrYmdHms, sFormatterForParsing);
					//}
					momentFrom = momentFrom.subtract(oVms.playbacktimeBase, 'minutes');
					momentTo = momentFrom.clone().add(oVms.playTime, 'minutes');
					
					let isValid = oCommon.validate.fromToMoment(momentTo, momentNow);
					if (!isValid) momentTo = momentNow;
				}
				this.#from = momentFrom.valueOf();
				this.#to = momentTo.valueOf();
                // console.log('-- CurrentEvent: From[%s] ~ To[%s], inheritSearchDatetimeYn: %s', momentFrom.format(sFormatter), momentTo.format(sFormatter), this.#inheritSearchDatetimeYn);
				// console.log('-- CurrentEvent: From[%s] ~ To[%s], isLfpEvent: %s, inheritSearchDatetimeYn: %s', momentFrom.format(sFormatter), momentTo.format(sFormatter), isLfpEvent, this.#inheritSearchDatetimeYn);
				// console.log('-- CurrentEvent: %s ~ %s, isLfpEvent: %s', momentFrom.format('YY-MM-DD HH:mm:ss'), momentTo.format('YY-MM-DD HH:mm:ss'), isLfpEvent);
				// console.log('-- CurrentEvent: %s ~ %s', momentFrom.format('YY-MM-DD HH:mm:ss'), momentTo.format('YY-MM-DD HH:mm:ss'));
			} else {
				this.#mode = 'PLAY';
				this.#from = 0;
				this.#to = 0;
			}
		} else {
			this.clear();
		}

		return {
			//evtId: this.#evtId,
			//evtOcrNo: this.#evtOcrNo,
			dstrtCd: this.#dstrtCd,
			viewRqstNo: this.#viewRqstNo,
			evtOcrYmdHms: this.#evtOcrYmdHms,
			//dtctnYmdhms: this.#dtctnYmdhms,
			from: this.#from,
			to: this.#to,
			mode: this.#mode,
		}
	}
}
