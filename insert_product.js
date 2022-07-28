let img_selector1 = document.getElementById("fileToUpload1");
let image1 = document.getElementById("image1");
let img_selector2 = document.getElementById("fileToUpload2");
let image2 = document.getElementById("image2");
let img_selector3 = document.getElementById("fileToUpload3");
let image3 = document.getElementById("image3");
let img_brand_selector = document.getElementById("fileImgBrand");
let img_brand = document.getElementById("imageBrand");
let add_brand = document.getElementById("addBrand");
let add_type = document.getElementById("addType");
let modal_brand = document.getElementById("modalBrand");
let modal_type = document.getElementById("modalType");
img_selector1.addEventListener("change", function () {
    image1.src = URL.createObjectURL(event.target.files[0]);
});
img_selector2.addEventListener("change", function () {
    image2.src = URL.createObjectURL(event.target.files[0]);
});
img_selector3.addEventListener("change", function () {
    image3.src = URL.createObjectURL(event.target.files[0]);
});

add_brand.addEventListener("click", function(){
    modal_brand.style.display = "block";
});
add_type.addEventListener("click", function(){
    modal_type.style.display = "block";
});
window.onclick = function(event) {
    if (event.target == modal_brand) {
      modal_brand.style.display = "none";
    }
    if (event.target == modal_type) {
        modal_type.style.display = "none";
    }
}
img_brand_selector.addEventListener("change", function(){
    img_brand.src = URL.createObjectURL(event.target.files[0]);
});


