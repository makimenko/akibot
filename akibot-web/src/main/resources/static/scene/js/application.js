if (!Detector.webgl)
	Detector.addGetWebGLMessage();

$(document).ready(AkiEvents.onDocumentReady);
window.onerror = AkiEvents.onDocumentError;
document.addEventListener('touchstart', AkiEvents.onTouchStart, false);

function animate() {

	requestAnimationFrame(animate);

	AkiCursor.animateCursor();
	AkiScene.render();

	if (AkiScene.controls != null) {
		AkiScene.controls.update();
	}
}
