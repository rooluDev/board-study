<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Small JSP Popup Example</title>>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
</head>
<body>
<h1>Password Verification</h1>
<button id="verifyPasswordBtn">Verify Password</button>

<!-- 비밀번호 확인 창 -->
<div id="passwordVerificationModal" style="display: none;">
    <label for="passwordInput">Enter Password:</label>
    <input type="password" id="passwordInput">
    <button id="confirmPasswordBtn">Confirm</button>
    <button id="closeModalBtn">Close</button>
</div>

<script>
    function goToList() {
        location.href = "/board?command=list";
    }
    $(document).ready(function() {
        // 버튼을 클릭하면 모달 창을 띄움
        $("#verifyPasswordBtn").click(function() {
            $("#passwordVerificationModal").show();
        });

        // 확인 버튼을 클릭하면 AJAX 요청을 보냄
        $("#confirmPasswordBtn").click(function() {
            var enteredPassword = $("#passwordInput").val();

            // AJAX 요청
            $.ajax({
                type: "POST",
                url: "/board?command=passwordConfirm",
                data: { password: enteredPassword },
                success: function(response) {
                    if (response === "success") {
                        goToList();
                    } else {
                        alert("Password is incorrect!");
                    }

                    // 모달 창 숨기기
                    $("#passwordVerificationModal").hide();
                },
                error: function() {
                    alert("Error during password verification.");
                }
            });
        });

        // 모달 창 닫기 버튼
        $("#closeModalBtn").click(function() {
            $("#passwordVerificationModal").hide();
        });
    });


</script>
</body>
</html>
