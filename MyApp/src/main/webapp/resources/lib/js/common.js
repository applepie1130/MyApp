/*! Common v0.0.1 | (c) 2014, 2015 SungJun All rights reserved */
/* For using jQuery */
$J = $.noConflict();
/**
 * 	Implement SpinnerController
 */
var SpinnerController = (function() {
	function init () {
		this.opts = {
			lines		: 11,		// The number of lines to draw
			length		: 0,		// The length of each line
			width		: 5,		// The line thickness
			radius		: 15,		// The radius of the inner circle
			corners		: 1,		// Corner roundness (0..1)
			rotate		: 0,		// The rotation offset
			direction	: 1,		// 1: clockwise, -1: counterclockwise
			color		: '#000',	// #rgb or #rrggbb or array of colors
			speed		: 1.0,		// Rounds per second
			trail		: 42,		// Afterglow percentage
			shadow		: false,	// Whether to render a shadow
			hwaccel		: false,	// Whether to use hardware acceleration
			className	: 'spinner',	// The CSS class to assign to the spinner
			zIndex		: 2e9,		// The z-index (defaults to 2000000000)
			top			: 'auto',	// Top position relative to parent
			left		: 'auto'	// Left position relative to parent
		};
		this.spinner = new Spinner(this.opts).spin();
	}
	
	function start() {
		$J("#load-spinner").hide().append(this.spinner.el).fadeIn();
	};
	
	function stop() {
		$J("#load-spinner").delay(100).fadeOut(function() {
			$J(this).empty();
		});
	}
	
	return {
		"init"		: init,
		"start"		: start,
		"stop"	 	: stop
	}
}());
SpinnerController.init();
$J(document).ajaxStart(function() { SpinnerController.start(); });
$J(document).ajaxStop(function() { SpinnerController.stop(); });

/**
 * 	Implement StringUtils
 */
var StringUtils = function() {};

StringUtils.uuid = function() {
	try {
		var s = [];
		var hexDigits = "0123456789abcdef";
		for (var i = 0; i < 36; i++) {
		    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
		}
		s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
		s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
		s[8] = s[13] = s[18] = s[23] = "-";
		
		return s.join("");
	
	} catch (e) {
		console.log(e);
	}
};

StringUtils.isEmpty = function(param) {
	try {
		return $J.isEmptyObject(param);
	} catch (e) {
		console.log(e);
	}
};

/**
 * 	Implement Controller
 */
var Controller = (function() {
	function init() {
		this.async			= true;
		this.method			= "POST";
		this.dataType		= "JSON";
		this.contentType	= "";
		this.requestId		= StringUtils.uuid();
		this.url			= "";
		this.data			= "";
		this.processData	= true;
	};
	
	function setAction( prmUrl ) {
		this.url = prmUrl;
	};
	
	function setParams( prm, type ) {
		if ( typeof type !== "undefined" && type.toUpperCase() === "JSON" ) {
			this.contentType	= "application/json; charset=UTF-8";
			this.data			= JSON.stringify(prm);
			this.dataType		= "JSON";
			this.processData	= true;
		} else if ( typeof type !== "undefined" && type.toUpperCase() === "HTML" ) {
			this.contentType	= "application/x-www-form-urlencoded; charset=UTF-8";
			this.data			= prm;
			this.dataType		= type.toUpperCase();
			this.processData	= true;
		} else if ( typeof type !== "undefined" && type.toUpperCase() === "FILE" ) {
			this.contentType	= "application/x-www-form-urlencoded; charset=UTF-8";
			this.data			= prm;
			this.dataType		= "JSON";
			this.processData	= false;
		} else {
			this.contentType	= "application/x-www-form-urlencoded; charset=UTF-8";
			this.data			= prm;
			this.dataType		= "JSON";
			this.processData	= true;
		}
	};
	
	function submit( callback ) {
		$J.ajax({
    		url			: this.url,
    		type		: this.method,
    		dataType	: this.dataType,
    		contentType : this.contentType,
    		processData : this.processData,
    		data		: this.data,
    		async		: this.async,
    		headers		: { requestId : this.requestId },
    		success		: function(data, status, jqXHR) {
    			var rtnData = data;
    			try {
    				callback(rtnData, status, jqXHR);
    			} catch(e) {
    				console.log("e:", e);
    			}
    		},
    		error : function(jqXHR, textStatus, errorThrown) {
    			try {
    				var sCallbackMsgs;
    				
    				if ( jqXHR.responseText.indexOf("<html>") < 0 ) {
    					sCallbackMsgs = decodeURIComponent(jqXHR.responseText) + "\n" + "(" + decodeURIComponent(jqXHR.statusText) + ", " + jqXHR.status + ")";
    				} else {
    					sCallbackMsgs = "오류가 발생했습니다.\n잠시 후 다시 시도해주세요." + "\n" + "(" + decodeURIComponent(jqXHR.statusText) + ", " + jqXHR.status + ")";
    				}
    				
    				alert(sCallbackMsgs);
    				
    			} catch(e) {
    				console.log("e:", e);
    			}
    		}
    	}); 
	};
	
	return {
		"init"				: init,
		"setAction"			: setAction,
		"setParams"	 		: setParams,
		"submit"			: submit
	}
}());
Controller.init();

/**
 * 	Implement PageReplaceForm
 */
var PageReplaceForm = (function() {
	function setAction( prmUrl ) {
		Controller.setAction( prmUrl );
	};
	
	function setParams( prm ) {
		Controller.setParams( prm, "html" );
	};
	
	function submit( callback ) { 
		Controller.submit( callback );
	};
	
	return {
		"setAction"			: setAction,
		"setParams"	 		: setParams,
		"submit"			: submit
	}
}());	


/**
 * 	Implement FileUploadConfig
 */
var FileUploader = (function() {
	function submit( prmUrl ) {
		return {
			url			: prmUrl, 
		    dataType	: 'json',
		    add			: function(e, data){
		    	var uploadFile	= data.files[0];
		        var isValid		= true;
		        
		        if (!(/png|jpe?g|gif/i).test(uploadFile.name)) {
		            alert('png, jpg, gif 만 가능합니다');
		            isValid		= false;
		            
		        } else if (uploadFile.size > 5000000) {
		            alert('파일 용량은 5MB를 초과할 수 없습니다.');
		            isValid		= false;
		            
		        }
		        
		        if (isValid) {
		            data.submit();
		        }
		    },
		    done: function (e, data) {
		    	console.log(data.result.fileName);
		    	console.log(data.result.fileSize);
		    	console.log(data.result.fileType);
		    },
		    fail: function(e, data){
		    	sCallbackMsgs = "오류가 발생했습니다.\n잠시 후 다시 시도해주세요.";			        	
		    }
		}
	}
	return {
		"submit"	: submit
	}
}());