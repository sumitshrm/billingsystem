
	
	function selectAggreement(){
		var selected_agg = $('#select_agg').val();
		if(selected_agg!=""){
			$('#agg_description').html(eval("aggreements."+selected_agg+".nameOfWork"));
			$('#estimated_cost').val(eval("aggreements."+selected_agg+".estimatedCost"));
			$('#tender_cost').val(eval("aggreements."+selected_agg+".tenderCost"));
		}else{
			$('#agg_description').html("-");
			$('#estimated_cost').val("");
			$('#tender_cost').val("");
		}
		
		
	}
	
	function calculate(){
		console.log("calculating");
		
		//CLear
		$('#dis_ae_max_power').html("");
		$('#dis_ee_max_power').html("");
		$('#dis_se_max_power').html("");
		$('#dis_ae_actual_power').html("");
		$('#dis_ee_actual_power').html("");
		$('#dis_se_actual_power').html("");
		$('#dis_ce_actual_power').html("");
		$('#eis_ae_max_power').html("");
		$('#eis_ee_max_power').html("");
		$('#eis_se_max_power').html("");
		$('#eis_ae_actual_power').html("");
		$('#eis_ee_actual_power').html("");
		$('#eis_se_actual_power').html("");
		$('#eis_ce_actual_power').html("");
		
		
		var estimated_cost = $('#estimated_cost').val();
		var tender_cost = $('#tender_cost').val();
		var dis_amount = $('#dis_amount').val();
		var eis_amount = $('#eis_amount').val();
		if(estimated_cost==""){
			//alert("estimated cost is required");
			//return;
		}
		if(tender_cost==""){
			//alert("tender cost is required");
			//return;
		}
		var validval = dis_amount+eis_amount+"";
		if(validval==""){
			//alert("eis amount or dis amount is required");
			//return;
		}
		if(eis_amount>0){
			calculateEIS();
		}
		if(dis_amount>0){
			calculateDIS();
		}
		
	}
	
	
	function calculateEIS(){
		console.log("calculating EIS");
		var estimated_cost = $('#estimated_cost').val();
		var tender_cost = $('#tender_cost').val();
		var dis_amount = $('#dis_amount').val();
		var eis_amount = $('#eis_amount').val();
		var filter_range = {"AE":{}, "EE":{}, "SE":{}, "CE":{}};
		console.log("master", eis_master_range);
		eis_master_range.data.forEach(function(item, index){
			  if(index==0){return;}
			  if(item[0]=="AE" && estimated_cost >= item[1] && estimated_cost <= item[2]){
				  filter_range.AE.maxpower = item[4];
				  filter_range.AE.power= tender_cost*(item[3]/100)> item[4] ? item[4] : tender_cost*(item[3]/100);
			  }
			  else if(item[0]=="EE" && estimated_cost >= item[1] && estimated_cost <= item[2]){
				  filter_range.EE.maxpower = item[4];
				  filter_range.EE.power= tender_cost*(item[3]/100)> item[4] ? item[4] : tender_cost*(item[3]/100);
			  }
			  else if(item[0]=="SE" && estimated_cost >= item[1] && estimated_cost <= item[2]){
				  filter_range.SE.maxpower = item[4];
				  filter_range.SE.power= tender_cost*(item[3]/100)> item[4] ? item[4] : tender_cost*(item[3]/100);
			  }else{
				  //filter_range.CE={"power" : item[3], "maxpower" : item[4]}
			  }
		});
		
		//set max_poweers
		$('#eis_ae_max_power').html("");
		$('#eis_ee_max_power').html("");
		$('#eis_se_max_power').html("");
		
		$('#eis_ae_max_power').html(filter_range.AE.maxpower);
		$('#eis_ee_max_power').html(filter_range.EE.maxpower);
		$('#eis_se_max_power').html(filter_range.SE.maxpower);
		// id="ies_ae_max_power" 
		
		//distribute EIS amount
		if(eis_amount>0){
			filter_range.AE.actual = eis_amount>filter_range.AE.power?filter_range.AE.power:eis_amount;
			eis_amount=eis_amount-filter_range.AE.power;
			if(eis_amount>0){
				filter_range.EE.actual = eis_amount>filter_range.EE.power?filter_range.EE.power:eis_amount;
				eis_amount=eis_amount-filter_range.EE.power;
			}
			if(eis_amount>0){
				filter_range.SE.actual = eis_amount>filter_range.SE.power?filter_range.SE.power:eis_amount;
				eis_amount=eis_amount-filter_range.SE.power;
			}
			if(eis_amount>0){
				filter_range.CE.actual = eis_amount;
			}
		}
		console.log("filtered", filter_range);
		$('#eis_ae_actual_power').html("");
		$('#eis_ee_actual_power').html("");
		$('#eis_se_actual_power').html("");
		$('#eis_ce_actual_power').html("");
		$('#eis_ae_actual_power').html(filter_range.AE.actual);
		$('#eis_ee_actual_power').html(filter_range.EE.actual);
		$('#eis_se_actual_power').html(filter_range.SE.actual);
		$('#eis_ce_actual_power').html(filter_range.CE.actual);
	}
	
	function calculateDIS(){
		console.log("calculating DIS");
		var estimated_cost = $('#estimated_cost').val();
		var tender_cost = $('#tender_cost').val();
		var dis_amount = $('#dis_amount').val();
		var eis_amount = $('#eis_amount').val();
		var filter_range = {"AE":{}, "EE":{}, "SE":{}, "CE":{}};
		console.log("master", dis_master_range);
		dis_master_range.data.forEach(function(item, index){
			  if(index==0){return;}
			  if(item[0]=="AE" && estimated_cost >= item[1] && estimated_cost <= item[2]){
				  filter_range.AE.maxpower = item[4];
				  filter_range.AE.power= tender_cost*(item[3]/100)> item[4] ? item[4] : tender_cost*(item[3]/100);
			  }
			  else if(item[0]=="EE" && estimated_cost >= item[1] && estimated_cost <= item[2]){
				  filter_range.EE.maxpower = item[4];
				  filter_range.EE.power= tender_cost*(item[3]/100)> item[4] ? item[4] : tender_cost*(item[3]/100);
			  }
			  else if(item[0]=="SE" && estimated_cost >= item[1] && estimated_cost <= item[2]){
				  filter_range.SE.maxpower = item[4];
				  filter_range.SE.power= tender_cost*(item[3]/100)> item[4] ? item[4] : tender_cost*(item[3]/100);
			  }else{
				  //filter_range.CE={"power" : item[3], "maxpower" : item[4]}
			  }
		});
		
		//set max_poweers
		
		
		$('#dis_ae_max_power').html(filter_range.AE.maxpower);
		$('#dis_ee_max_power').html(filter_range.EE.maxpower);
		$('#dis_se_max_power').html(filter_range.SE.maxpower);
		// id="ies_ae_max_power" 
		
		//distribute EIS amount
		if(dis_amount>0){
			filter_range.AE.actual = dis_amount>filter_range.AE.power?filter_range.AE.power:dis_amount;
			dis_amount=dis_amount-filter_range.AE.power;
			if(dis_amount>0){
				filter_range.EE.actual = dis_amount>filter_range.EE.power?filter_range.EE.power:dis_amount;
				dis_amount=dis_amount-filter_range.EE.power;
			}
			if(dis_amount>0){
				filter_range.SE.actual = dis_amount>filter_range.SE.power?filter_range.SE.power:dis_amount;
				dis_amount=dis_amount-filter_range.SE.power;
			}
			if(dis_amount>0){
				filter_range.CE.actual = dis_amount;
			}
		}
		console.log("filtered", filter_range);
		$('#dis_ae_actual_power').html("");
		$('#dis_ee_actual_power').html("");
		$('#dis_se_actual_power').html("");
		$('#dis_ce_actual_power').html("");
		$('#dis_ae_actual_power').html(filter_range.AE.actual);
		$('#dis_ee_actual_power').html(filter_range.EE.actual);
		$('#dis_se_actual_power').html(filter_range.SE.actual);
		$('#dis_ce_actual_power').html(filter_range.CE.actual);
	}
	
	$(document).ready(function(){
		  // Add smooth scrolling to all links
		  $("#a_calculate").on('click', function(event) {
			  console.log("click");
		    // Make sure this.hash has a value before overriding default behavior
		    if (this.hash !== "") {
		      // Prevent default anchor click behavior
		      event.preventDefault();

		      // Store hash
		      var hash = this.hash;

		      // Using jQuery's animate() method to add smooth page scroll
		      // The optional number (800) specifies the number of milliseconds it takes to scroll to the specified area
		      $('html, body').animate({
		        scrollTop: $(hash).offset().top
		      }, 800, function(){
		   
		        // Add hash (#) to URL when done scrolling (default click behavior)
		        window.location.hash = hash;
		      });
		    } // End if
		  });
		});
	