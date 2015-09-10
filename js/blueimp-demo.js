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
		.append($('<img>').prop('src', 'img/images/T-20150215.JPG').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150215.JPG')
		.prop('title', '2015.02.15: Initial assembling of delivered parts. Proof Of Concept. The decision: to go forward.')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20150316.JPG').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150316.JPG')
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
		.append($('<img>').prop('src', 'img/images/T-20150602-1.JPG').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150602-1.JPG')
		.prop('title', '2015.06.02: Copper Printed Circuit Board Etching')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20150602-2.JPG').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150602-2.JPG')
		.prop('title', '2015.06.02: PCB Drilling')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20150602-3.JPG').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150602-3.JPG')
		.prop('title', '2015.06.02: Printed Circuit Board (PCB) for AkiBot 0.1')
		.attr('data-gallery', '')
		.appendTo(linksContainer);
	$('<a/>')
		.append($('<img>').prop('src', 'img/images/T-20150625-1.JPG').prop('class','img-thumbnail'))
		.prop('href', 'img/images/20150625-1.JPG')
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
		

});
