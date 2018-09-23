

	  /* contact form validation */
	  var Validator = function(formObject) {
	    this.form = $(formObject);
	    var Elements = {
	      name: {
	        reg: /^(?=.{4,}).+/,
	        require: true,
	        error: "name should be of atleast 4 characters",
	      },

	      email: {
	        reg: /^[a-z-0-9_+.-]+\@([a-z0-9-]+\.)+[a-z0-9]{2,7}$/i,
	        error: "entered e-mail address is not valid",
	      },
	      phone: {
	        reg: /^\d{10}$/,
	        error: "entered phone number is not valid",
	      },
	      password: {
		        reg: /^(?=.{6,}).+/,
		        error: "password should be atleast 6 characters",
		      },
	      message: {
	        reg: /^(?!\s*$).+/,
	        error: "field cannot be empty.",
	      },
	      gender: {
	        error: "gender is required",
	      },
	      selectOption: {
	        error: "this field is required",
	        required: true
	      }
	    };

	    var handleError = function(element, message) {
	      element.addClass('input-error');
	      var $err_msg = element.parent('div');
	      $err_msg.find('.error').remove();

	      var error = $('<div class="error"></div>').text(message);
	      error.appendTo($err_msg);
	      console.log(element);


	      element.on('keypress change', function() {
	        $(error).fadeOut(100, function() {
	          console.log(element);
	          element.removeClass('input-error');
	        });
	      });
	      /*element.on('keypress change', function() {
		          element.removeClass('input-error');
		      });*/

	    };

	    /* Select Option */
	    var validatePassword = function(){
	    	var password = $("#password");
	        var repeatpassword = $("#repeatpassword");
	        console.log("password", repeatpassword.val());
	        if(password.val()!=repeatpassword.val()){
		        handleError(repeatpassword, "password and repeat password did not match");
		        return false;
	        }
	        return true;
	    };
	    
	    this.validate = function() {
	      var errorCount = 0;
	      
	      if(!validatePassword()){
	    	  errorCount++;
	      }
	      
	      this.form.find("select").each(function(index, field) {
	        var type = $(field).data("validation");
	        var validation = Elements[type];
	        if ($(field).val() == "") {
	          errorCount++;
	          handleError($(field), validation.error);
	        }
	      });

	      this.form.find("input, textarea").each(function(index, field) {
	        var type = $(field).data("validation");
	        var validation = Elements[type];
	        if (validation !== undefined) {
	          var re = new RegExp(validation.reg);
	          if (validation) {
	            if (!re.test($(field).val())) {
	              errorCount++;
	              handleError($(field), validation.error);
	            }
	          }
	        }
	      })

	      /* Radio button */

	      var radioList = $('input:radio');
	      var radioNameList = new Array();
	      var radioUniqueNameList = new Array();
	      var notCompleted = 0;
	      for (var i = 0; i < radioList.length; i++) {
	        radioNameList.push(radioList[i].name);
	      }
	      radioUniqueNameList = jQuery.unique(radioNameList);
	      console.log(radioUniqueNameList);
	      for (var i = 0; i < radioUniqueNameList.length; i++) {
	        var field = $('#' + radioUniqueNameList[i]);
	        var type = field.data("validation");
	        var validation = Elements[type];
	        if ($('input[name=' + type + ']:checked', '#test').val() == undefined) {
	          errorCount++;
	          handleError($(field), validation.error);
	        }
	      }

	      return errorCount == 0;
	    };
	  };

