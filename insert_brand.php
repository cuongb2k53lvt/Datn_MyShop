<?php
    require "../dbconnect.php";
    $target_dir = "../brand_logo/";
    $base_link = "http://localhost/FashionShop-phpServer/brand_logo/";
    $brand_name = $_POST["inputAddBrandName"];
    $brand_location = $_POST["inputAddBrandLocation"];
    $brand_descr = $_POST["inputAddBrandDesc"];
    $file_size = $_FILES["fileImgBrand"]["size"];
    $logo_basename = $brand_name.basename($_FILES["fileImgBrand"]["name"]);
    $target_file = $target_dir.$logo_basename;
    $uploadOk = 1;
    // Check if image file is a actual image or fake image
    if(isset($_POST["submitAddBrand"])){
        $check = getimagesize($_FILES["fileImgBrand"]["tmp_name"]);
        if($check !== false) {
            echo "File is an image ";
            $uploadOk = 1;
          } else {
            echo "File is not an image.";
            $uploadOk = 0;
          }
    }
    CheckFile($target_file,$file_size);
    if($uploadOk == 0){
        echo '<script type="text/JavaScript"> 
        alert("File chưa được upload");
        window.open("http://localhost/FashionShop-phpServer/Web2/product_info_page.php","_self");
      </script>';
    }else{
        if(strlen($brand_name) > 0 && strlen($brand_location) > 0 && strlen($brand_descr) > 0){
            move_uploaded_file($_FILES["fileImgBrand"]["tmp_name"], $target_file);
            $query = "INSERT INTO Brand_FashionShop VALUES (null, '$brand_name','$base_link$logo_basename','$brand_location','$brand_descr')";
            mysqli_query($connect,$query);
            echo '<script type="text/JavaScript"> 
            alert("Thêm brand thành công");
            window.open("http://localhost/FashionShop-phpServer/Web2/product_info_page.php","_self");
        </script>';
        }
    }
    
    function CheckFile($target_file, $file_size){
        $imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));
        // Check if file already exists
        if (file_exists($target_file)) {
            echo '<script type="text/JavaScript"> 
            alert("File ảnh đã tồn tại");
            window.open("http://localhost/FashionShop-phpServer/Web2/product_info_page.php","_self");
        </script>';
            $uploadOk = 0;
        }
        // Check file size
        if ($file_size > 2000000) {
            echo '<script type="text/JavaScript"> 
            alert("File ảnh quá nặng");
            window.open("http://localhost/FashionShop-phpServer/Web2/product_info_page.php","_self");
        </script>';
            $uploadOk = 0;
        }
        // Allow certain file formats
        if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg"
            && $imageFileType != "gif" ) {
            echo '<script type="text/JavaScript"> 
            alert("Chỉ sử dụng ảnh có định dạng JPG, JPEG, PNG & GIF");
            window.open("http://localhost/FashionShop-phpServer/Web2/product_info_page.php","_self");
        </script>';
            $uploadOk = 0;
        }
    }
?>