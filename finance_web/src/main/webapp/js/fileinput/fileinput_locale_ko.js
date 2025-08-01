(function ($) {
    "use strict";

    $.fn.fileinputLocales['ko'] = {
        fileSingle: '파일',
        filePlural: '파일',
        browseLabel: '파일선택',
        removeLabel: '삭제',
        removeTitle: '선택된 파일 삭제',
        cancelLabel: '취소',
        cancelTitle: '전송취소',
        uploadLabel: '파일전송',
        uploadTitle: '선택한 파일 전송',
        msgNo: '아니요',
        msgCancelled: '취소',
        msgZoomTitle: '미리보기',
        msgZoomModalHeading: '파일상세',
        msgSizeTooLarge: '파일"{name}" (<b>{size} KB</b>)의 용량이 업로드 가능 용량(<b>{maxSize} KB</b>)을 초과하였습니다.',
        msgFilesTooLess: '최소<b>{n}</b>개의 {files}을 선택해 주세요.',
        msgFilesTooMany: '선택한 파일의 수<b>({n}개)</b>는 업로드 가능한 수량<b>({m}개)</b>을 초과하였습니다.',
        msgFileNotFound: '파일"{name}"이 존재하지 않습니다.',
        msgFileSecured: '파일"{name}"에 대한 권한이 없습니다.',
        msgFileNotReadable: '파일"{name}"을 읽을수 없습니다.',
        msgFilePreviewAborted: '파일"{name}"의 미리보기가 중지되었습니다.',
        msgFilePreviewError: '파을"{name}"을 읽는 중에 에러가 발생하였습니다.',
        msgInvalidFileType: '"{name}"은 유효하지 않은 파일 형식입니다.。"{types}"형식만 지원하고 있습니다.',
        msgInvalidFileExtension: '"{name}"는 유효하지 않은 파일 확장자입니다.확장자가"{extensions}"인 파일만 지원하고 있습니다.',
        msgUploadAborted: '파일전송이 중지 되었습니다.',
        msgValidationError: '파일삭제 후 다시 선택해 주세요.',
        msgLoading: '{files}개의 파일중{index}번째 파일을 읽는 중...',
        msgProgress: '{files}개의 파일중{index}번째 파일을 읽는 중 - {name} - {percent}% 완료',
        msgSelected: '{n}개의 {files}을 선택',
        msgFoldersNotAllowed: 'Drag & drop files only! {n} folder(s) dropped were skipped.',
        msgImageWidthSmall: 'Width of image file "{name}" must be at least {size} px.',
        msgImageHeightSmall: 'Height of image file "{name}" must be at least {size} px.',
        msgImageWidthLarge: 'Width of image file "{name}" cannot exceed {size} px.',
        msgImageHeightLarge: 'Height of image file "{name}" cannot exceed {size} px.',
        msgImageResizeError: 'Could not get the image dimensions to resize.',
        msgImageResizeException: 'Error while resizing the image.<pre>{errors}</pre>',
        dropZoneTitle: '파일을 선택해 주세요.',
        fileActionSettings: {
            removeTitle: '파일삭제',
            uploadTitle: '파일전송',
            indicatorNewTitle: '아직 전송되지 않았습니다.',
            indicatorSuccessTitle: '전송완료',
            indicatorErrorTitle: '전송실패',
            indicatorLoadingTitle: '전송중...'
        }
    };
})(window.jQuery);
