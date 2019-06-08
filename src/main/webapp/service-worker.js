// Copyright 2016 Google Inc.
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//      http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

var dataCacheName = 'matcons-v1';
var cacheName = 'matconsPWA';
var filesToCache = [
  '/resources/dojo/dojo.js',
  '/resources/spring/Spring.js',
  '/resources/spring/Spring-Dojo.js',
  '/resources/script/jqwidgets/styles/jqx.base.css',
  '/resources/script/jqwidgets/styles/jqx.ui-smoothness.css',
  '/resources/script/jqwidgets/styles/jqx.bootstrap.css',
  '/resources/styles/breadcrum-navigation.css',
  '/resources/script/jquery/jquery-1.11.1.min.js',
  '/resources/script/jqwidgets/jqxcore.js',
  '/resources/script/jqwidgets/jqxbuttons.js',
  '/resources/script/jqwidgets/jqxinput.js',
  '/resources/script/jqwidgets/jqxnotification.js',
  '/resources/assets/vendor/bootstrap/css/bootstrap.min.css',
  '/resources/assets/vendor/metisMenu/metisMenu.min.css',
  '/resources/assets/adminlte/css/AdminLTE.min.css',
  '/resources/assets/adminlte/css/skins/_all-skins.min.css',
  '/resources/assets/vendor/font-awesome/css/font-awesome.min.css',
  '/resources/assets/vendor/bootstrap/js/bootstrap.min.js',
  '/resources/dijit/themes/dijit.css',
  
  '/resources/assets/adminlte/js/app.min.js',
  '/resources/assets/vendor/bootbox/bootbox.min.js',
  '/resources/dijit/themes/tundra/tundra.css',
  '/resources/images/favicon.ico',
  '/resources/dijit/form/ComboBox.js',
  '/resources/dijit/form/FilteringSelect.js',
  '/resources/dijit/form/SimpleTextarea.js',
  '/resources/dojo/data/util/simpleFetch.js',
  '/resources/dojo/data/util/sorter.js',
  '/resources/dojo/data/util/filter.js',
  '/resources/dijit/themes/tundra/images/spriteArrows.png',
  '/resources/dijit/themes/tundra/images/validationInputBg.png',
  '/resources/dijit/themes/tundra/images/buttonEnabled.png',
  '/resources/dijit/form/nls/ComboBox.js',
  '/resources/images/loading.gif',
  '/resources/assets/vendor/datatables-plugins/dataTables.bootstrap.css',
  '/resources/assets/vendor/datatables-responsive/dataTables.responsive.css',
  '/resources/assets/vendor/datatables/js/jquery.dataTables.min.js',
  '/resources/assets/adminlte/img/blank-avatar.png',
  '/resources/assets/vendor/datatables-plugins/dataTables.bootstrap.min.js',
  '/resources/assets/vendor/datatables-responsive/dataTables.responsive.js',
  '/resources/assets/vendor/font-awesome/fonts/fontawesome-webfont.woff2?v=4.6.3',
  '/resources/assets/vendor/bootstrap/fonts/glyphicons-halflings-regular.woff2',
  '/resources/assets/vendor/images/sort_both.png',
  '/resources/dijit/TitlePane.js',
  '/resources/dojo/nls/dojo_en-us.js',
  '/resources/script/jqwidgets/jqxdata.js',
  '/resources/script/jqwidgets/jqxscrollbar.js',
  '/resources/script/jqwidgets/jqxdatatable.js',
  '/resources/script/jqwidgets/jqxlistbox.js',
  '/resources/script/jqwidgets/jqxdropdownlist.js',
  '/resources/script/jqwidgets/jqxdata.export.js',
  '/resources/script/jqwidgets/styles/images/icon-right.png',
  '/resources/script/jqwidgets/styles/images/icon-left.png',
  '/resources/script/jqwidgets/styles/images/icon-down.png',
  '/resources/script/jqwidgets/styles/images/icon-up.png',
  '/resources/script/jqwidgets/styles/images/loader.gif',
  '/resources/script/jqwidgets/styles/images/close.png',
  '/resources/script/jqwidgets/styles/images/search.png',
  '/resources/script/jqwidgets/jqxmenu.js',
  '/resources/script/jqwidgets/jqxgrid.js',
  '/resources/script/jqwidgets/jqxgrid.selection.js',
  '/resources/script/jqwidgets/jqxgrid.filter.js',
  '/resources/script/jqwidgets/jqxcheckbox.js',
  '/resources/script/jqwidgets/jqxgrid.pager.js',
  '/resources/script/jqwidgets/jqxgrid.edit.js',
  '/resources/script/jqwidgets/jqxgrid.columnsresize.js',
  '/resources/script/jqwidgets/jqxwindow.js',
  '/resources/script/jquery/jquery-ui.min.js',
  '/resources/script/jquery.ui-contextmenu.min.js',
  '/resources/styles/jquery-ui.css',
  '/resources/assets/vendor/bootbox/bootbox.min.js',
  '/aggreements/schedule/v1/2018'
];

self.addEventListener('install', function(e) {
  console.log('[ServiceWorker] Install');
  e.waitUntil(
    caches.open(cacheName).then(function(cache) {
      console.log('[ServiceWorker] Caching app shell');
      return cache.addAll(filesToCache).catch(console.log('drror'));
    })
  );
});

self.addEventListener('activate', function(e) {
  console.log('[ServiceWorker] Activate');
 /* e.waitUntil(
    caches.keys().then(function(keyList) {
      return Promise.all(keyList.map(function(key) {
        if (key !== cacheName && key !== dataCacheName) {
          console.log('[ServiceWorker] Removing old cache', key);
          return caches.delete(key);
        }
      }));
    })
  );*/
  /*
   * Fixes a corner case in which the app wasn't returning the latest data.
   * You can reproduce the corner case by commenting out the line below and
   * then doing the following steps: 1) load app for first time so that the
   * initial New York City data is shown 2) press the refresh button on the
   * app 3) go offline 4) reload the app. You expect to see the newer NYC
   * data, but you actually see the initial data. This happens because the
   * service worker is not yet activated. The code below essentially lets
   * you activate the service worker faster.
   */
 /* return self.clients.claim();*/
});

self.addEventListener('fetch', function(e) {
  console.log('[Service Worker] Fetch', e.request.url);
  e.respondWith(
	      caches.match(e.request).then(function(response) {
	        return response || fetch(e.request);
	      })
	    );
  
  /*var dataUrl = 'https://query.yahooapis.com/v1/public/yql';
  if (e.request.url.indexOf(dataUrl) > -1) {*/
    /*
     * When the request URL contains dataUrl, the app is asking for fresh
     * weather data. In this case, the service worker always goes to the
     * network and then caches the response. This is called the "Cache then
     * network" strategy:
     * https://jakearchibald.com/2014/offline-cookbook/#cache-then-network
     */
    /*e.respondWith(
      caches.open(dataCacheName).then(function(cache) {
        return fetch(e.request).then(function(response){
          cache.put(e.request.url, response.clone());
          return response;
        });
      })
    );
  } else {*/
    /*
     * The app is asking for app shell files. In this scenario the app uses the
     * "Cache, falling back to the network" offline strategy:
     * https://jakearchibald.com/2014/offline-cookbook/#cache-falling-back-to-network
     */
    
 /* }*/
});
