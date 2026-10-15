// Opens the smell's code example in its native <dialog>; closes on button, backdrop click or Esc.
document.querySelectorAll('.example-trigger').forEach(function (trigger) {
    var dialog = document.getElementById(trigger.dataset.dialog);
    trigger.addEventListener('click', function () { dialog.showModal(); });
    dialog.querySelector('.example-close').addEventListener('click', function () { dialog.close(); });
    dialog.addEventListener('click', function (e) { if (e.target === dialog) dialog.close(); });
});
