var app = angular.module('myApp', []);


var lat_js;
var lon_js;

var lat_here;
var lon_here;

window.onload = function(){
    document.getElementById("next").style.display="none";
    document.getElementById("pre").style.display="none";
    var json;
    document.getElementById("progress_bar").style.display = "none";

    document.getElementById("submit").disabled = true;
    var json1;
    $.ajax({
    	 	url: "http://ip-api.com/json",
    	   	type: "GET",
    	   	success: function(data) {
    	     	json1 = data;
                console.log(json1);
                lat_here = json1.lat;
                lon_here = json1.lon;
                console.log(lat_here);
                console.log(lon_here);
                document.getElementById("submit").disabled = false;
    	   	},
            error: function() {
                alert("Failed to get geolocation!");
            }
    	 });

};



var placeSearch, autocomplete1;
// src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBbOgCGopbCyS-j1o95fBXGY8Oy0QnulAM&libraries=places&libraries=places&callback=initAutocomplete";

     // function initAutocomplete() {

       // autocomplete1 = new google.maps.places.Autocomplete((document.getElementById('autocomplete')),
       //     {types: ['geocode']});
     //  autocomplete.addListener('place_changed', fillInAddress);

     // }


     function geolocate() {
         // src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBbOgCGopbCyS-j1o95fBXGY8Oy0QnulAM&libraries=places";
       if (navigator.geolocation) {
         navigator.geolocation.getCurrentPosition(function(position) {
           var geolocation = {
             lat: position.coords.latitude,
             lng: position.coords.longitude
           };
           var circle = new google.maps.Circle({
             center: geolocation,
             radius: position.coords.accuracy
           });
           autocomplete1.setBounds(circle.getBounds());
         });
       }
     }

     function fillInAddress() {

        var place = autocomplete1.getPlace();
        console.log(place);
        if(place != undefined){
            document.getElementById("autocomplete").value = place.formatted_address;
            console.log(place.formatted_address);
            return place.formatted_address;
        }
      }





var token;
var pageNum;
var max;
var html_array= [];
var information_array=[];
var getAddr;

app.run(function ($rootScope) {

        $rootScope.clear= function () {
            console.log("112233");
            html_array= [];
            information_array=[];
            document.getElementById("head_show").innerHTML = '';
            document.getElementById("table1").innerHTML = '';
            // document.getElementById("pre").style.display = "none";
            // document.getElementById("next").style.display = "none";
        };
    });

app.controller("myctrl", function($scope, $http) {

    $scope.submitForm = function() {
        // $scope.address = fillInAddress();
        // console.log($scope.address);
        var addr;
        if( autocomplete1.getPlace() != undefined ){
            addr = fillInAddress();
        }else{
            addr = $scope.address;
        }

        console.log(addr);

        getAddr = function(){
            return addr;
        };





        document.getElementById("progress_bar").style.display = "block";


        if(document.getElementById("radio1").checked == true) {
            lat_js = lat_here;
            lon_js = lon_here;
        }
        if(document.getElementById("radio2").checked == true) {
            lat_js = "";
            lon_js = "";
        }
        $http({
            method: 'GET',
            url: "http://zhuoyiny-php.us-east-2.elasticbeanstalk.com/app.js?keyword="+$scope.keyword+"&category="+$scope.category+"&radius="+$scope.radius+"&lat_js="+lat_js+"&lon_js="+lon_js+"&address="+addr,
            //data: $.param($scope.data),
            // headers: {
            //     'Content-Type': 'application/x-www-form-urlencoded'
            // }
        }).then(function mySuccess(response) {
            document.getElementById("progress_bar").style.display = "none";
              console.log(response);
              $scope.jsondata = response.data;
              console.log($scope.jsondata);
              // console.log($scope.jsondata[0]);
              // console.log($scope.jsondata[1]);
              // console.log($scope.jsondata[2]);
              // console.log($scope.jsondata[3]);
              // console.log($scope.jsondata[4]);

              token = $scope.jsondata[8];
              pageNum = 1;
              max=1;
              information_array.push($scope.jsondata);
              genTable1($scope.jsondata, pageNum);
              if(token==null){
                  document.getElementById("next").style.display="none";
                  document.getElementById("pre").style.display="none";
              }else{
                  document.getElementById("next").style.display="block";
              }

            }, function myError(response) {
              console.log(response.statusText);
              var html_text = '<div class="alert alert-danger" role="alert">Failed to get search results.</div>';
              document.getElementById("table1").innerHTML = html_text;
          });
    };


});



    function genTable1(data, pageNum) {
        var html_text = "";
        if(data[2].length==0){
            html_text += '<div class="container">';
            html_text += '<br><div class="alert alert-warning" role="alert">';
            html_text += 'No records.';
            html_text += '</div>';
        }else{
            html_text += '<div class="container">';
            html_text += '<div class="text-right"><button type="" class="btn btn-default" style="background-color:white; border:1px solid rgb(200,200,200)">Details <i class="fa fa-chevron-right"></i></button></div>';
            html_text += '<table class="table table-hover">';
            html_text += "<tbody>";
            html_text += "<thead>";
            html_text += "<tr>";
            html_text += "<th>#</th>";
            html_text += "<th>Category</th>";
            html_text += "<th>Name</th>";
            html_text += "<th>Address</th>";
            html_text += "<th>Favorite</th>";
            html_text += "<th>Details</th>";
            html_text += "</tr>";
            html_text += "</thead>";
            for (i = 0; i < data[2].length; i++) {

                html_text += "<tr>";
                html_text += "<td>"+((pageNum-1)*20+(i+1))+"</td>";
                html_text += "<td><img src="+ data[2][i] +" height=30 width=30></td>";
                html_text += '<td>' + data[3][i] + "</td>";
                html_text += '<td id="str'+i+'">' + data[4][i] + "</td>";
                html_text += '<td><button class="btn" onclick="favorite_list(this.id)" style="background-color:white; border:1px solid rgb(200,200,200)"><i class="fa fa-star-o"></i></button></td>';
                html_text += '<td><button id="'+i+'" class="btn" onclick="information(this.id, pageNum);" style="background-color:white; border:1px solid rgb(200,200,200)"><i class="fa fa-chevron-right"></i></button></td>';
                // style="border:1px solid rgb(210,210,210);border-radius:5px;padding:4px 6px;"
                html_text += "</tr>";
            }
            html_text += "</tbody>";
            html_text += "</table>";
        }
        html_text += "</div>";
        // console.log(html_text);
        document.getElementById("table1").innerHTML=html_text;
        html_array.push(html_text);
    }




        var getPlaceId;
        var info_json;
        var getInfoJson;
        information = function(clicked_id, pageNum){


            console.log(clicked_id);
            var place_id = information_array[pageNum-1][5][clicked_id];
            console.log(place_id);

            $.ajax({
                url: "http://zhuoyiny-php.us-east-2.elasticbeanstalk.com/information?place_id="+place_id,
                type: "GET",

                success: function(data) {
                    var info_json = data;
                    console.log(info_json);
                    getInfoJson = function(){
                        return info_json;
                    };

                    getPlaceId = function(){
                        return place_id;
                    };

                    headShow(clicked_id, pageNum, info_json);
                }
            });


        };


    var reviews_json;
    // var clicked_id;
    headShow = function(clicked_id, pageNum, info_json){
        // var InfoJson = getInfoJson();
        console.log(info_json);
        console.log(information_array[pageNum-1][3]);
        console.log(clicked_id);
        console.log(information_array[pageNum-1][3][clicked_id]);
        var URL;
        if(info_json[5]==null) {
            URL = info_json[4];
        }else{
            URL = info_json[5];
        }
        reviews_json = info_json[8];
        console.log("11111");
        console.log(reviews_json);
        var html_text = '<h3 style="text-align:center">'+information_array[pageNum-1][3][clicked_id]+"</h3>";

        html_text +='<div class="container">';
        html_text +='<br><button type="" class="btn btn-default" style="background-color:white; border:1px solid rgb(200,200,200)"><i class="fa fa-chevron-left"></i> List</button>';
        html_text +='<button class="btn pull-right" style="border:1px solid rgb(200,200,200);padding:0;"><a href="https://twitter.com/intent/tweet?text=Check%20out%20'+information_array[pageNum-1][3][clicked_id]+'%20located%20at%20'+information_array[pageNum-1][4][clicked_id]+'.%20Website:%20&url='+URL+'&hashtags=%20TravelAndEntertainmentSearch"><img src="http://cs-server.usc.edu:45678/hw/hw8/images/Twitter.png" alt="" height=37 width=37></a></button>';
        html_text +='<button class="btn pull-right" onclick="favorite_list(clicked_id, pageNum)" style="background-color:white;margin-right:8px;border:1px solid rgb(200,200,200);"><i class="fa fa-star-o"></i></button>';
        html_text +='<br>';


        html_text +='<br>';
        html_text +='<ul class="nav nav-tabs justify-content-end" role="tablist">';
        html_text +='<li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#info">Info</a></li>';
        html_text +='<li class="nav-item"><a id="'+clicked_id+'" onclick="photos(this.id, pageNum)" class="nav-link" data-toggle="tab" href="#photos">Photos</a></li>';
        html_text +='<li class="nav-item"><a id="'+clicked_id+'" onclick="showmap(this.id, pageNum)" class="nav-link" data-toggle="tab" href="#map">Map</a></li>';
        html_text +='<li class="nav-item"><a id="'+clicked_id+'" onclick="reviews(this.id, pageNum)" class="nav-link" data-toggle="tab" href="#reviews">Reviews</a></li>';
        html_text +='</ul>';
        html_text +='<div class="tab-content">';
        html_text +='<div id="info" class="container tab-pane active"><br>';
        html_text +='<div id="info_table"></div>';
        html_text +='</div>';
        html_text +='<div id="falsemap"></div><div id="photos" class="container tab-pane fade"><br>';
        html_text +='<div id="photos_table"></div>';
        html_text +='</div>';
        html_text +='<div id="map" class="container tab-pane fade"><br>';
        html_text +='<div id="map_table"></div>';
        html_text +='</div>';
        html_text +='<div id="reviews" class="container tab-pane fade"><br>';
        html_text +='<div id="reviews_table_twobtn"></div>';
        html_text +='<div id="reviews_table"></div>';
        html_text +='</div>';
        html_text +='</div>';
        html_text +='</div>';
        html_text +='';

        document.getElementById("head_show").innerHTML=html_text;
        // document.getElementById("head_show").style.display = "block";
        genInfoTable();

    };



    genInfoTable = function(){
        var priceLevel;
        var hours;
        var info_json = getInfoJson();

        if(info_json[2]==1){
            priceLevel = "$";
        }else if(info_json[2]==2){
            priceLevel = "$$";
        }else if(info_json[2]==3){
            priceLevel = "$$$";
        }else if(info_json[2]==4){
            priceLevel = "$$$$";
        }else{
            priceLevel = "";
        }

        var myDate = new Date();
        var dayofweek = myDate.getDay(); //获取当前星期X(0-6,0代表星期天)
        console.log("dayofweek: "+dayofweek);
        var str0;
        var str1;
        var str2;
        var str3;
        var str4;
        var str5;
        var str6;
        console.log(info_json);
        console.log(info_json[6]);
        var date_arr;

        if(info_json[6] != null){
            if(info_json[6].open_now == false){
                hours = 'Closed ';
            }else{
                var str = info_json[6].weekday_text[dayofweek];
                hours = 'Open now: '+str.substring(str.indexOf(' ')+1,str.length)+'  ';

            }
            str0 = info_json[6].weekday_text[0];
            str1 = info_json[6].weekday_text[1];
            str2 = info_json[6].weekday_text[2];
            str3 = info_json[6].weekday_text[3];
            str4 = info_json[6].weekday_text[4];
            str5 = info_json[6].weekday_text[5];
            str6 = info_json[6].weekday_text[6];
            var arr0 = str0.split(": ");
            var arr1 = str1.split(": ");
            var arr2 = str2.split(": ");
            var arr3 = str3.split(": ");
            var arr4 = str4.split(": ");
            var arr5 = str5.split(": ");
            var arr6 = str6.split(": ");
            date_arr = [arr6, arr0, arr1, arr2, arr3, arr4, arr5];
            // console.log('str0: '+str0);
        }

        var starTotal = 5;
        var starPercentage = (info_json[3] / starTotal) * 100;

        var html_text = '<table class="table table-striped">';
        html_text +='<tbody>';
        html_text +='<tr><th scope="row">Address</th><td>'+info_json[0]+'</td></tr>';
        if(info_json[1] != null){
            html_text +='<tr><th scope="row">Phone Number</th><td>'+info_json[1]+'</td></tr>';
        }
        if(info_json[2] != null){
            html_text +='<tr><th scope="row">Price Level</th><td>'+priceLevel+'</td></tr>';
        }
        if(info_json[3] != null){
            html_text +='<tr><th scope="row">Rating</th><td>'+info_json[3]+'<div class="stars-outer"><div class="stars-inner" id="stars-inner"></div></div></td></tr>';
        }
        if(info_json[4] != null){
            html_text +='<tr><th scope="row">Google Page</th><td><a href="'+info_json[4]+'" target="_blank">'+info_json[4]+'</a></td></tr>';
        }
        if(info_json[5] != null){
            html_text +='<tr><th scope="row">Website</th><td><a href="'+info_json[5]+'" target="_blank">'+info_json[5]+'</a></td></tr>';
        }
        if(info_json[6] != null){
            html_text +='<tr><th scope="row">Hours</th><td>'+ hours +'<a href="" data-toggle="modal" data-target="#myModal">Daily open hours</a></td></tr>';
        }
        html_text +='</tbody>';
        html_text +='</table>';

        html_text +='<div class="container">';
        html_text +='<div class="modal fade" id="myModal">';
        html_text +='<div class="modal-dialog">';
        html_text +='<div class="modal-content">';
        html_text +='<div class="modal-header">';
        html_text +='<h5 class="modal-title">Open hours</h5>';
        html_text +='<button type="button" class="close" data-dismiss="modal">&times;</button>';
        html_text +='</div>';
        html_text +='<div class="modal-body">';
        html_text +='<div class="container">';
        html_text +='<table class="table">';
        html_text +='<tbody>';
        if(info_json[6] != null){
            html_text +='<tr><td><b>'+date_arr[dayofweek][0]+'</b></td><td><b>'+date_arr[dayofweek][1]+'</b></td></tr>';
            for(var i=dayofweek+1; i<7; i++){
                html_text +='<tr><td>'+date_arr[i][0]+'</td><td>'+date_arr[i][1]+'</td></tr>';
            }
            for(var j=0; j<dayofweek; j++){
                html_text +='<tr><td>'+date_arr[j][0]+'</td><td>'+date_arr[j][1]+'</td></tr>';
            }
        }
        html_text +='</tbody>';
        html_text +='</table>';
        html_text +='</div>';
        html_text +='<div class="modal-footer">';
        html_text +='<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>';
        html_text +='</div>';
        html_text +='</div>';
        html_text +='</div>';
        html_text +='</div>';
        html_text +='</div>';
        html_text +='</div>';
        document.getElementById("info_table").innerHTML=html_text;
        if(document.getElementById("stars-inner") != null) {
            document.getElementById("stars-inner").style.width = starPercentage+"%";
        }
    };

    photos = function(clicked_id, pageNum){

        var request = {
          placeId: getPlaceId()
        };

        service = new google.maps.places.PlacesService(falsemap);
        service.getDetails(request, callback);

        var url;
        var url_array=[];

        function callback(place, status) {
          if (status == google.maps.places.PlacesServiceStatus.OK) {
            var photos = place.photos;
            var html_text = '';
            if(photos != undefined) {
                for(var i=0; i<photos.length; i++){
                    url=photos[i].getUrl({'maxWidth': 1600, 'maxHeight': 1600});
                    url_array.push(url);
                }
                console.log(url_array);
                html_text = '<div class="card-columns" id="card-columns">';
                for(var j=0; j < url_array.length; j++){
                    html_text += '<div style="float:left">';
                    html_text += '<div class="card p-1"><a href="'+url_array[j]+'" target="_blank"><img class="card-img-top" src='+url_array[j]+'></a></div>';
                    html_text += '</div>';
                }
                html_text += '</div>';
            }else{
                html_text += '<br><br><div class="alert alert-warning" role="alert">';
                html_text += 'No records.';
                html_text += '</div>';
            }
            document.getElementById("photos_table").innerHTML=html_text;
          }
        }

    };


var des_lat;
var des_lon;
var lat_org;
var lon_org;
// var map1;
function showmap(clicked_id, pageNum){

    var info_json = getInfoJson();
    var name = information_array[pageNum-1][3][clicked_id];
    var formatted_address = info_json[0];
    console.log("name:   "+information_array[pageNum-1][3][clicked_id]);
    console.log(formatted_address);

    var html_text ='<form id="myForm">';
    html_text +='';
    html_text +='<div class="form-row">';
    html_text +='<div class="form-group col-md-4">';
    html_text +='<label for="from">From</label>';
    html_text +='<input type="text" class="form-control" id="from" value="My location" onFocus="geolocate2()" placeholder="Your location">';
    html_text +='</div>';
    html_text +='<div class="form-group col-md-4">';
    html_text +='<label for="inputZip">To</label>';
    html_text +='<input type="text" class="form-control" id="inputZip" placeholder="'+name+', '+formatted_address+'" disabled>';
    html_text +='</div>';
    html_text +='<div class="form-group col-md-2">';
    html_text +='<label for="inputState">Travel Mode</label>';
    html_text +='<select id="mode" class="form-control">';
    html_text +='<option value="DRIVING" selected>Driving</option>';
    html_text +='<option value="BICYCLING">Bicycling</option>';
    html_text +='<option value="TRANSIT">Transit</option>';
    html_text +='<option value="WALKING">Walking</option>';
    html_text +='</select>';
    html_text +='</div>';
    html_text +='<div class="form-group col-md-2">';
    html_text +='<button type="button" id="submit" onclick="submitMap();" class="btn btn-primary" style="margin-top:32px;">Get Directions</button>';
    html_text +='</div>';
    html_text +='</div>';
    html_text +='<button type="button" style="padding:0;" id="pano_icon" onclick="show_pano()" class="btn btn-default"><img src="http://cs-server.usc.edu:45678/hw/hw8/images/Pegman.png" alt="" style="height:37px;width:37px;"></button>';
    html_text +='<button type="button" style="padding:0;" id="map_icon" onclick="show_map()" class="btn btn-default"><img src="http://cs-server.usc.edu:45678/hw/hw8/images/Map.png" alt="" style="height:37px;width:37px;"></button>';

    html_text +='</form>';
    // html_text +='<div style="background-color:yellow;">';
    html_text +='<div id="map1pano_position"></div>';
    // html_text +='</div>';
    html_text +='<div id="directionsPanel_position"></div>';
    html_text +='';
    html_text +='';
    html_text +='';
    html_text +='';

    document.getElementById("map_table").innerHTML=html_text;

    var autocomplete2 = new google.maps.places.Autocomplete(document.getElementById('from'));

    function geolocate2() {
        // src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBbOgCGopbCyS-j1o95fBXGY8Oy0QnulAM&libraries=places";
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
          var geolocation = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
          };
          var circle = new google.maps.Circle({
            center: geolocation,
            radius: position.coords.accuracy
          });
          autocomplete2.setBounds(circle.getBounds());
        });
      }
    }

    des_lat = information_array[pageNum-1][6][clicked_id];
    des_lon = information_array[pageNum-1][7][clicked_id];

    console.log("des_lat:"+des_lat);
    console.log("des_lon:"+des_lon);

    show_map();

}

    function show_map(){
        var html_text ='<div id="map1" style="height:400px; width:100%;"></div>';
        document.getElementById("map1pano_position").innerHTML=html_text;

        var fenway = {lat: parseFloat(des_lat), lng: parseFloat(des_lon)};
        map1 = new google.maps.Map(document.getElementById('map1'), {
          center: fenway,
          zoom: 14
        });
        var marker = new google.maps.Marker({
          position: fenway,
          visible: true,
          map: map1
        });
        document.getElementById("map_icon").style.display = "none";
        document.getElementById("pano_icon").style.display = "block";
    }

    function show_pano(){
        var html_text ='<div id="pano" style="height:400px; width:100%;"></div>';
        document.getElementById("map1pano_position").innerHTML=html_text;
        var fenway = {lat: parseFloat(des_lat), lng: parseFloat(des_lon)};
        map1 = new google.maps.Map(document.getElementById('pano'), {
          center: fenway,
          zoom: 14
        });
        var marker = new google.maps.Marker({
          position: fenway,
          visible: true,
          map: map1
        });
        var panorama = new google.maps.StreetViewPanorama(
            document.getElementById('pano'), {
              position: fenway,
              pov: {
                heading: 34,
                pitch: 10
              }
            });
        map1.setStreetView(panorama);
        document.getElementById("map_icon").style.display = "block";
        document.getElementById("pano_icon").style.display = "none";
        document.getElementById("directionsPanel_position").innerHTML = '';
    }


    function submitMap(){
        console.log("directions Display");

        var fenway = {lat: parseFloat(des_lat), lng: parseFloat(des_lon)};
        map1 = new google.maps.Map(document.getElementById('map1'), {
          center: fenway,
          zoom: 14
        });
        var marker = new google.maps.Marker({
          position: fenway,
          visible: true,
          map: map1
        });

        if(document.getElementById("radio1").checked == true){
            console.log("radio1checked");
            if(document.getElementById("from").value =='My location'){
                lat_org = lat_here;
                lon_org = lon_here;
                console.log("lat_org:"+lat_org);
                console.log("lon_org:"+lon_org);
                getRoute();
            }else{
                var from = document.getElementById("from").value;
                $.ajax({
                    url: "http://zhuoyiny-php.us-east-2.elasticbeanstalk.com/map?from="+from,
                    type: "GET",

                    success: function(data) {
                        var ar = data;
                        console.log(ar);
                        newGeoArr = ar;
                        console.log("new address entered!");
                        lat_org = newGeoArr[0];
                        lon_org = newGeoArr[1];
                        console.log("lat_org:"+lat_org);
                        console.log("lon_org:"+lon_org);
                        getRoute();
                    },
                    error: function() {
                        alert("Get geolocation error!");
                    }
                });
            }
        }else if(document.getElementById("radio2").checked == true){
            console.log("radio2checked");
            document.getElementById("from").value = getAddr();
            lat_org = information_array[pageNum-1][0];
            lon_org = information_array[pageNum-1][1];

            console.log("lat_org:"+lat_org);
            console.log("lon_org:"+lon_org);
            getRoute();
        }

        function getRoute(){
            var directionsDisplay = new google.maps.DirectionsRenderer;
            var directionsService = new google.maps.DirectionsService;
            console.log(directionsDisplay);
            console.log(directionsService);


            directionsDisplay.setMap(map1);

            calculateAndDisplayRoute(directionsService, directionsDisplay);
            var html_text ='<div id="directionsPanel" style="width:100%;height 100%"></div>';
            document.getElementById("directionsPanel_position").innerHTML = html_text;
            directionsDisplay.setPanel(document.getElementById('directionsPanel'));


            function calculateAndDisplayRoute(directionsService, directionsDisplay) {
                var selectedMode = document.getElementById('mode').value;
                console.log(selectedMode);
                directionsService.route({
                    origin: {
                        lat: parseFloat(lat_org),
                        lng: parseFloat(lon_org)
                    },
                    destination: {
                        lat: parseFloat(des_lat),
                        lng: parseFloat(des_lon)
                    },
                    provideRouteAlternatives: true,
                    travelMode: google.maps.TravelMode[selectedMode]
                }, function(response, status) {
                    if (status == 'OK') {
                        directionsDisplay.setDirections(response);
                    } else {
                        window.alert('Directions request failed due to ' + status);
                    }
                });
            }
        }

    }




    var default_google_text;
    var default_yelp_text;
    var reviews_flag;
    reviews = function(clicked_id, pageNum){
        // console.log(pageNum);
        // console.log(reviews_json);
        console.log("000000000:   "+clicked_id);
        reviews_flag = "google";

        var html_text = '<div class="dropdown" style="display: inline-block;">';
        html_text += '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" id="review_btn1">Google Reviews</button>';
        html_text += '<div class="dropdown-menu">';
        html_text += '<a class="dropdown-item" onclick="google_reviews()">Google Reviews</a>';
        html_text += '<a  id="'+clicked_id+'" class="dropdown-item" onclick="yelp_reviews(this.id)">Yelp Reviews</a>';
        html_text += '</div>';
        html_text += '</div>';
        html_text += '<div class="dropdown" style="display: inline-block;margin-left:13px;">';
        html_text += '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" id="review_btn2">Default Order</button>';
        html_text += '<div class="dropdown-menu">';
        html_text += '<a class="dropdown-item" onclick="default_order()">Default Order</a>';
        html_text += '<a class="dropdown-item" onclick="highest_rating()">Highest Rating</a>';
        html_text += '<a class="dropdown-item" onclick="lowest_rating()">Lowest Rating</a>';
        html_text += '<a class="dropdown-item" onclick="most_recent()">Most Recent</a>';
        html_text += '<a class="dropdown-item" onclick="least_recent()">Least Recent</a>';
        html_text += '</div>';
        html_text += '</div>';
        document.getElementById("reviews_table_twobtn").innerHTML=html_text;

        if(reviews_json == null){
            var html_text1 = '<br><br><div class="alert alert-warning" role="alert">';
            html_text1 += 'No records.';
            html_text1 += '</div>';
            document.getElementById("reviews_table").innerHTML=html_text1;
            default_google_text = html_text1;
        }else{
            var html_text_add = genGoogleReviewTable();
            document.getElementById("reviews_table").innerHTML=html_text_add;
            default_google_text = html_text_add;
            }
            // console.log("55555555:    "+html_text_add);
    };


    function google_reviews(){
        document.getElementById("review_btn1").innerHTML="Google Reviews";
        document.getElementById("reviews_table").innerHTML=default_google_text;
        reviews_flag = "google";
    }

    function yelp_reviews(clicked_id){
        document.getElementById("review_btn1").innerHTML="Yelp Reviews";
        reviews_flag = "yelp";
        // var xhr = new XMLHttpRequest();
        var info_json = getInfoJson();

        var formatted_address = info_json[0];
        var arr = formatted_address.split(',');
        var name = information_array[pageNum-1][3][clicked_id];
        console.log(information_array[pageNum-1][3][clicked_id]);
        console.log(clicked_id);
        var address = '';
        if(arr.length ==4){
            address = arr[arr.length-4].trim();
        }
        var city = arr[arr.length-3].trim();
        var state = arr[arr.length-2].trim().substr(0,2);
        var country = arr[arr.length-1].trim();

        $.ajax({
            url: "http://zhuoyiny-php.us-east-2.elasticbeanstalk.com/yelp?name="+name+"&address="+address+"&city="+city+"&state="+state+"&country="+country,
            type: "GET",

            success: function(data) {
                var ar = data;
                console.log(ar);
                if(ar == ''){
                    console.log("no yelp reviews.");
                    var html_text = '<br><br><div class="alert alert-warning" role="alert">';
                    html_text += 'No records.';
                    html_text += '</div>';
                    document.getElementById("reviews_table").innerHTML = html_text;
                }else{
                    genYelpReviews(ar);
                }
            },
            error: function() {
                var html_text = '<br><div class="alert alert-warning" role="alert">';
                html_text += 'No records.';
                html_text += '</div>';
                document.getElementById("reviews_table").innerHTML = html_text;
            }
        });

    }

    var yelp_json;
    var getYelpJson;
    function genYelpReviews(id){

        $.ajax({
            url: "http://zhuoyiny-php.us-east-2.elasticbeanstalk.com/yelpreviews?id="+id,
            type: "GET",

            success: function(data) {
                yelp_json = data;
                console.log(yelp_json);
                getYelpJson = function(){
                    return yelp_json;
                };

                var html_text_add = genYelpReviewTable();
                document.getElementById("reviews_table").innerHTML=html_text_add;
                default_yelp_text = html_text_add;
                //
                // document.getElementById("reviews_table").innerHTML=html_text;
                // default_yelp_text = html_text;
            }
        });

    }


    function genYelpReviewTable(){
        // yelp_json[i].rating;
        // yelp_json[i].text;
        // yelp_json[i].url;
        // yelp_json[i].time_created;
        // yelp_json[i].user.image_url;
        // yelp_json[i].user.name;
        var yelp_json = getYelpJson();
        var html_text ='';
        for(var i=0; i<yelp_json.length; i++){
            var num = yelp_json[i].rating;
            var star_show='';
            for( var j=0; j<num; j++){
                star_show += '<i class="fa fa-star" style="color:orange;"></i>';
            }

            var newTime = yelp_json[i].time_created;
            html_text += '<div class="media" style="border:1px solid rgb(200,200,200);">';
            html_text += '<a href="' + yelp_json[i].url + '" target="_blank"><img class="mr-3 rounded-circle" src = "' + yelp_json[i].user.image_url + '" alt="" height=60 width=60></a>';
            html_text += '<div class="media-body">';
            html_text += '<h5 class="mt-0"><a href="' + yelp_json[i].url + '" target="_blank">' + yelp_json[i].user.name + '</a></h5>';
            html_text += '<div style="color:rgb(134,142,150);">' + star_show + newTime + '</div>';
            html_text += '<p>' + yelp_json[i].text + '</p>';
            html_text += '</div>';
            html_text += '</div>';

        }
        return html_text;
    }


    function default_order(){
        document.getElementById("review_btn2").innerHTML="Default Order";
        if(reviews_flag == "google"){
            document.getElementById("reviews_table").innerHTML=default_google_text;
        }else{
            document.getElementById("reviews_table").innerHTML=default_yelp_text;
        }
    }


    function genGoogleReviewTable(){

        var html_text='';
        for(var i=0; i<reviews_json.length; i++){
            var num = reviews_json[i].rating;
            var star_show='';
            for( var j=0; j<num; j++){
                star_show += '<i class="fa fa-star" style="color:orange;"></i>';
            }
            var utcSeconds = reviews_json[i].time;
            var newTime = moment(utcSeconds*1000).format('YYYY-MM-DD HH:mm:ss');
            // console.log(newTime);
            html_text += '<div class="media" style="border:1px solid rgb(200,200,200);">';
            html_text += '<a href="' + reviews_json[i].author_url + '" target="_blank"><img class="mr-3 rounded-circle" src = "' + reviews_json[i].profile_photo_url + '" alt="" height=60 width=60></a>';
            html_text += '<div class="media-body">';
            html_text += '<h5 class="mt-0"><a href="' + reviews_json[i].author_url + '" target="_blank">' + reviews_json[i].author_name + '</a></h5>';
            html_text += '<div style="color:rgb(134,142,150);">' + star_show + newTime + '</div>';
            html_text += '<p>' + reviews_json[i].text + '</p>';
            html_text += '</div>';
            html_text += '</div>';
        }
         // console.log("444444:      "+html_text);
        return html_text;
    }



    highest_rating = function(){
        document.getElementById("review_btn2").innerHTML="Highest Rating";
        function compare(a,b) {
          if (a.rating < b.rating)
            return 1;
          if (a.rating > b.rating)
            return -1;
          return 0;
        }
        var html_text_add;
        if(reviews_flag == "google"){
            reviews_json.sort(compare);
            // console.log(reviews_json);
            html_text_add = genGoogleReviewTable();
        }else{
            yelp_json.sort(compare);
            // console.log(yelp_json);
            html_text_add = genYelpReviewTable();
        }

        document.getElementById("reviews_table").innerHTML=html_text_add;
    };


    lowest_rating = function(){
        document.getElementById("review_btn2").innerHTML="Lowest Rating";
        function compare(a,b) {
          if (a.rating < b.rating)
            return -1;
          if (a.rating > b.rating)
            return 1;
          return 0;
        }
        var html_text_add;
        if(reviews_flag == "google"){
            reviews_json.sort(compare);
            // console.log(reviews_json);
            html_text_add = genGoogleReviewTable();
        }else{
            yelp_json.sort(compare);
            // console.log(yelp_json);
            html_text_add = genYelpReviewTable();
        }

        document.getElementById("reviews_table").innerHTML=html_text_add;
    };

    least_recent = function(){
        document.getElementById("review_btn2").innerHTML="Least Recent";
        function compare_yelp(a,b) {
            var a_time = moment(a.time_created, 'YYYY-MM-DD HH:mm:ss').valueOf();
            var b_time = moment(b.time_created, 'YYYY-MM-DD HH:mm:ss').valueOf();
            console.log(a_time);
            console.log(b_time);
          if (a_time < b_time)
            return -1;
          if (a_time > b_time)
            return 1;
          return 0;
        }
        function compare_google(a,b) {
          if (a.time < b.time)
            return -1;
          if (a.time > b.time)
            return 1;
          return 0;
        }
        var html_text_add;
        if(reviews_flag == "google"){
            reviews_json.sort(compare_google);
            // console.log(reviews_json);
            html_text_add = genGoogleReviewTable();
        }else{
            yelp_json.sort(compare_yelp);
            // console.log(yelp_json);
            html_text_add = genYelpReviewTable();
        }

        document.getElementById("reviews_table").innerHTML=html_text_add;
    };


    most_recent = function(){
        document.getElementById("review_btn2").innerHTML="Most Recent";
        function compare_google(a,b) {
          if (a.time < b.time)
            return 1;
          if (a.time > b.time)
            return -1;
          return 0;
        }
        function compare_yelp(a,b) {
            var a_time = moment(a.time_created, 'YYYY-MM-DD HH:mm:ss').valueOf();
            var b_time = moment(b.time_created, 'YYYY-MM-DD HH:mm:ss').valueOf();
            console.log(a_time);
            console.log(b_time);
          if (a_time < b_time)
            return 1;
          if (a_time > b_time)
            return -1;
          return 0;
        }
        var html_text_add;
        if(reviews_flag == "google"){
            reviews_json.sort(compare_google);
            // console.log(reviews_json);
            html_text_add = genGoogleReviewTable();
        }else{
            yelp_json.sort(compare_yelp);
            // console.log(yelp_json);
            html_text_add = genYelpReviewTable();
        }

        document.getElementById("reviews_table").innerHTML=html_text_add;
    };



    showNextPage = function(){
        pageNum = pageNum + 1;
        console.log(token);
        console.log(pageNum);
        //console.log(html_array[pageNum-1]);
        if(html_array[pageNum-1]!=undefined){
            console.log("stored html");
            document.getElementById("table1").innerHTML=html_array[pageNum-1];
            if(token==null&&pageNum==max || pageNum==3){
                console.log("last page || page3");
                document.getElementById("next").style.display="none";
                document.getElementById("pre").style.display="block";

            }else if(pageNum==2){
                console.log("page2");
                document.getElementById("next").style.display="block";
                document.getElementById("pre").style.display="block";
            }else if(pageNum==1){
                console.log("page1");
                document.getElementById("next").style.display="block";
                document.getElementById("pre").style.display="none";
            }
        }else{
            max = pageNum;
            $.ajax({
                    url: "http://zhuoyiny-php.us-east-2.elasticbeanstalk.com/nextpage?token="+token,
                    type: "GET",

                    success: function(data) {

                        var ar = data;
                        console.log(ar);
                        token=ar[8];
                        console.log(token==null);

                        information_array.push(ar);
                        genTable1(ar,pageNum);


                        if(token==null || pageNum==3){
                            console.log("page3 / no more token");
                            document.getElementById("next").style.display="none";
                            document.getElementById("pre").style.display="block";

                        }else if(pageNum==2){
                            console.log("page2");
                            document.getElementById("next").style.display="block";
                            document.getElementById("pre").style.display="block";
                        }else if(pageNum==1){
                            console.log("page1");
                            document.getElementById("next").style.display="block";
                            document.getElementById("pre").style.display="none";
                        }
                    }
                });

        }
        console.log("html_array length: "+ html_array.length);
        console.log("information_array length: "+ information_array.length);
    };



    showPrevPage = function(){
        pageNum = pageNum -1;
        console.log(token);
        console.log(pageNum);
        document.getElementById("table1").innerHTML=html_array[pageNum-1];
        if(pageNum>1){
            document.getElementById("next").style.display="block";
            document.getElementById("pre").style.display="block";
        }else{
            document.getElementById("next").style.display="block";
            document.getElementById("pre").style.display="none";
        }
        console.log("html_array length: "+ html_array.length);
        console.log("information_array length: "+ information_array.length);
    };
