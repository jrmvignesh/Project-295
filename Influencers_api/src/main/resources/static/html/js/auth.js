/**
 * Created by Ramkumar on 5/15/2017.
 */


function navigateToUrl(e) {
    e.preventDefault();
    var source = event.target || event.srcElement;
    window.location=source.value;
}