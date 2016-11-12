/*
 * blueimp Gallery Demo JS 2.12.1
 * https://github.com/blueimp/Gallery
 *
 * Copyright 2013, Sebastian Tschan
 * https://blueimp.net
 *
 * Licensed under the MIT license:
 * http://www.opensource.org/licenses/MIT
 */

/* global blueimp, $ */

$(function () {
    'use strict';

	var linksContainer = $('#image-links');

	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20150215.jpg').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150215.jpg')
		.prop('title', '2015.02.15: Initial assembling of delivered parts. Proof Of Concept. The decision: to go forward.')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20150316.jpg').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150316.jpg')
		.prop('title', '2015.03.16: Most of components delivered from China. Initial wireless prototype with battery')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20150327.png').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150327.png')
		.prop('title', '2015.03.27: Printed Circuit Board (PCB) design in Zenit')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20150602-1.jpg').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150602-1.jpg')
		.prop('title', '2015.06.02: Copper Printed Circuit Board Etching')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20150602-2.jpg').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150602-2.jpg')
		.prop('title', '2015.06.02: PCB Drilling')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20150602-3.jpg').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150602-3.jpg')
		.prop('title', '2015.06.02: Printed Circuit Board (PCB) for AkiBot 0.1')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20150625-1.jpg').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150625-1.jpg')
		.prop('title', '2015.06.25: AkiBot v0.1 and YouTube demo')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20150625-JUnit.png').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150625-JUnit.png')
		.prop('title', '2015.06.25: Initial Unittests/Functional tests')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20150815_Web-BasicControl.png').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150815_Web-BasicControl.png')
		.prop('title', '2015.08.15: Swing replaced to Web (HTML5, JS, Bootstrap, WebSockets, REST, AJAX, JSON)')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20150908_LocationAreaGrid-2.png').prop('class','img-thumbnail'))	
		.prop('href', 'img/images/20150908_LocationAreaGrid-2.png')
		.prop('title', '2015.09.08: Location Area Grid Visualization in HTML5/WebGL/Three.js')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20160426.jpg').prop('class','img-thumbnail'))	
		.prop('href', 'img/images/20160426.jpg')
		.prop('title', '2016.04.25: 3D Printing office in Riga')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20160425.jpg').prop('class','img-thumbnail'))	
		.prop('href', 'img/images/20160425.jpg')
		.prop('title', '2016.04.25: AkiVaku 3D printed body, designed in Blender')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20160427.jpg').prop('class','img-thumbnail'))	
		.prop('href', 'img/images/20160427.jpg')
		.prop('title', '2016.04.27: AkiVaku complete body design in Blender')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20160428.jpg').prop('class','img-thumbnail'))	
		.prop('href', 'img/images/20160428.jpg')
		.prop('title', '2016.04.28: AkiVaku design in Blender')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20160505.jpg').prop('class','img-thumbnail'))	
		.prop('href', 'img/images/20160505.jpg')
		.prop('title', '2016.05.05: AkiVaku PCB modelling')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20160702.jpg').prop('class','img-thumbnail'))	
		.prop('href', 'img/images/20160702.jpg')
		.prop('title', '2016.07.02: Self-made PCB')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20161030.jpg').prop('class','img-thumbnail'))	
		.prop('href', 'img/images/20161030.jpg')
		.prop('title', '2016.10.30: Review of PCB Layers before sending to PCBWay.com (China)')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20161112.jpg').prop('class','img-thumbnail'))	
		.prop('href', 'img/images/20161112.jpg')
		.prop('title', '2016.11.12: Received PCB from PCBWay.com (China)')
		.attr('data-gallery', '')
		.appendTo(linksContainer);

		
		
		blueimp.Gallery([
        {
            title: 'AkiBot Demo 1 (eng)',
            type: 'text/html',
            youtube: 'ldJn0d0cMRI'
        }
    ], {
        container: '#blueimp-video-carousel',
        carousel: true
    });
	

});
