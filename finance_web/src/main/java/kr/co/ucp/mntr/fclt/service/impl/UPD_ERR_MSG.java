package kr.co.ucp.mntr.fclt.service.impl;

public enum UPD_ERR_MSG {

	REQUIERD {
		public String getMessage(String... args) {
			return "필수 입력 항목입니다.";
		}
	},
	INPUT_RESTRICT {
		public String getMessage(String... args) {
			return args[0] + " 만 입력 가능한 항목입니다.";
		}
	},
	NOT_EXIST_CODE {
		public String getMessage(String... args) {
			return "존재하지 않는 코드입니다.";
		}
	},
	DIGIT_RESTRICT {
		public String getMessage(String... args) {
			if (args.length == 1) return "중요 오류:" + args[0] + "자리 이하의 값만 입력 가능합니다.";
			return args[0] + "자리 부터 " + args[1] + "자리의 값만 입력 가능합니다.";
		}
	},
	RANGE_RESTRICT {
		public String getMessage(String... args) {
			return args[0] + " ~ " + args[1] + "의 값만 유효합니다.";
		}
	},
	DECIMAL_RESTRICT {
		public String getMessage(String... args) {
			return "소숫점 이하 " + args[0] + "자리 이상 입력 하여야 합니다.";
		}
	},
	LENGTH_RESTRICT {
		public String getMessage(String... args) {
			if (args.length == 1) return args[0] + "자리 이하의 값만 입력 가능합니다.";
			return args[0] + "자리 부터 " + args[1] + "자리의 값만 입력 가능합니다.";
		}
	},

	FORMAT_RESTRICT {
		public String getMessage(String... args) {
			return "\"" + args[0] + "\"의 형식으로 입력해 주세요.";
		}
	},
	CENTER_CODE_RESTRICT {
		public String getMessage(String... args) {
			return "센터코드를 확인해 주세요.";
		}
	},
	NOT_EXIST_PRNT {
		public String getMessage(String... args) {
			return "등록되지 않은 시설물 아이디 입니다.";
		}
	},
	DATE_CONVERT_ERROR {
		public String getMessage(String... args) {
			return "데이터 변환 도중에 에러가 발생하였습니다.";
		}
	},
	GET_DATA_ERROR {
		public String getMessage(String... args) {
			return "데이터 취득 도중에 에러가 발생하였습니다.";
		}
	};

	public abstract String getMessage(String... args);
}
