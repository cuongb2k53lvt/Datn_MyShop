<?php
    require "../dbconnect.php";
    $target_dir = "../product_image/";
    $base_link = "http://localhost/FashionShop-phpServer/product_image/";
    $cur_date = date("Y-m-d");
    $product_id = '';
    $product_name = $_POST["productName"];
    $material = $_POST["material"];
    $location = $_POST["location"];
    $description = $_POST["description"];
    $price = $_POST["price"];
    $cost = $_POST["cost"];
    $type = $_POST["type"];
    $brand = $_POST["brand"];
    $discount = $_POST["discount"];
    $size_s = $_POST["size_s"];
    $size_m = $_POST["size_m"];
    $size_l = $_POST["size_l"];
    $size_xl = $_POST["size_xl"];
    $file_size1 = $_FILES["fileToUpload1"]["size"];
    $file_size2 = $_FILES["fileToUpload2"]["size"];
    $file_size3 = $_FILES["fileToUpload3"]["size"];
    $basename1 = $product_name.basename($_FILES["fileToUpload1"]["name"]);
    $basename2 = $product_name.basename($_FILES["fileToUpload2"]["name"]);
    $basename3 = $product_name.basename($_FILES["fileToUpload3"]["name"]);
    $target_file1 = $target_dir.$basename1;
    $target_file2 = $target_dir.$basename2;
    $target_file3 = $target_dir.$basename3;
    $uploadOk = 1;
    $insertProductOk = 1;
    $insertSizeOk = 0;
    $error = "";
    $query_product = 'SELECT * FROM Products_FashionShop';
    $data_product = mysqli_query($connect,$query_product);
    // Check if image file is a actual image or fake image
    if(isset($_POST["submit"])){
        $check1 = getimagesize($_FILES["fileToUpload1"]["tmp_name"]);
        $check2 = getimagesize($_FILES["fileToUpload2"]["tmp_name"]);
        $check3 = getimagesize($_FILES["fileToUpload3"]["tmp_name"]);
        if($check1 !== false && $check2 !== false && $check3 !== false) {
            // echo '<script type="text/JavaScript"> 
            //         alert("File is an image '.$brand.$type.'");
            //         window.open("https://cuongb2k53lvt.000webhostapp.com/FashionShop-phpServer/Web2/product_info_page.php","_self");
            //       </script>';
            $uploadOk = 1;
          } else {
            echo '<script type="text/JavaScript"> 
                    alert("File is not an image '.$brand.$type.'");
                    window.open("http://localhost/FashionShop-phpServer/Web2/product_info_page.php","_self");
                  </script>';
            $uploadOk = 0;
          }
    }
    CheckFile($target_file1,$file_size1);
    CheckFile($target_file2,$file_size2);
    CheckFile($target_file3,$file_size3);
    $query = "INSERT INTO products_fashionshop VALUES (null,'$type','$brand','$product_name','$description','$price','$cost','$cur_date',5,0,'$location','$material','Còn hàng')";
    $data = mysqli_query($connect,$query);
    if($data){
        $insertProductOk = 1;
        $data_product = mysqli_query($connect,$query_product);
        while($row = mysqli_fetch_assoc($data_product)){
            if($row['product_name'] == $product_name){
                $product_id = $row['product_id'];
            }
        }
    }else{
        $insertProductOk = 0;
    }

    // Check if $uploadOk, $insertProductOk is set to 0 by an error
    if ($uploadOk == 0 || $insertProductOk == 0) {
        echo '<script type="text/JavaScript"> 
                    alert("Sorry, your file was not uploaded '.$insertProductOk.mysqli_error($connect).'");
                    window.open("http://localhost/FashionShop-phpServer/Web2/product_info_page.php","_self");
                  </script>';
    // if everything is ok, try to upload file
    } else {
        $insertSizeOk = InsertQuantityProduct("S",$size_s,$product_id,$connect);
        $insertSizeOk += InsertQuantityProduct("M",$size_m,$product_id,$connect);
        $insertSizeOk += InsertQuantityProduct("L",$size_l,$product_id,$connect);
        $insertSizeOk += InsertQuantityProduct("XL",$size_xl,$product_id,$connect);
        if (move_uploaded_file($_FILES["fileToUpload1"]["tmp_name"], $target_file1)
        && move_uploaded_file($_FILES["fileToUpload2"]["tmp_name"], $target_file2)
        && move_uploaded_file($_FILES["fileToUpload3"]["tmp_name"], $target_file3)
        && $insertSizeOk == 4) {
            InsertPhotoProduct($basename1,$product_id,$connect,$base_link);
            InsertPhotoProduct($basename2,$product_id,$connect,$base_link);
            InsertPhotoProduct($basename3,$product_id,$connect,$base_link);
            echo '<script type="text/JavaScript"> 
                    alert("The product has been uploaded.");
                    window.open("http://localhost/FashionShop-phpServer/Web2/product_info_page.php","_self");
                  </script>';
        } else {
            unlink("../product_image/".$basename1);
            unlink("../product_image/".$basename2);
            unlink("../product_image/".$basename3);
            $delete_quantity_product = "DELETE FROM product_size_fashionshop WHERE product_id = '$product_id'";
            $delete_product = "DELETE FROM products_fashionshop WHERE product_id = '$product_id'";
            mysqli_query($connect,$delete_quantity_product);
            mysqli_query($connect,$delete_product);
            echo '<script type="text/JavaScript"> 
                    alert("Sorry, there was an error '.$product_id.mysqli_error($connect).'");
                    window.open("http://localhost/FashionShop-phpServer/Web2/product_info_page.php","_self");
                  </script>';
        }
    }

    function InsertPhotoProduct($basename, $product_id, $connect, $base_link){
        $query = "INSERT INTO product_photo_fashionshop VALUES (null,'$product_id','$base_link$basename')";
        mysqli_query($connect,$query);
    }

    function InsertQuantityProduct($size, $quantity, $product_id, $connect){
        if(strlen($quantity) == 0){
            $quantity = 0;
        }
        $query = "INSERT INTO product_size_fashionshop VALUES (null,'$product_id','$size','$quantity','$quantity')";
        $data = mysqli_query($connect,$query);
        if($data){
            return 1;
        }else{
            return 0;
        }
    }

    function CheckFile($target_file, $file_size){
        $imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));
        // Check if file already exists
        if (file_exists($target_file)) {
            echo '<script type="text/JavaScript"> 
                    alert("Sorry, file already exists.");
                    window.open("http://localhost/FashionShop-phpServer/Web2/product_info_page.php","_self");
                  </script>';
            $uploadOk = 0;
        }
        // Check file size
        if ($file_size > 2000000) {
            echo '<script type="text/JavaScript"> 
                    alert("Sorry, your file is too large.");
                    window.open("http://localhost/FashionShop-phpServer/Web2/product_info_page.php","_self");
                  </script>';
            $uploadOk = 0;
        }
        // Allow certain file formats
        if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg"
            && $imageFileType != "gif" ) {
            echo '<script type="text/JavaScript"> 
                    alert("Sorry, only JPG, JPEG, PNG & GIF files are allowed.");
                    window.open("http://localhost/FashionShop-phpServer/Web2/product_info_page.php","_self");
                  </script>';
            $uploadOk = 0;
        }
    }
?>