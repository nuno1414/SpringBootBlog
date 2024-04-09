// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
//        fetch(`/api/articles/${id}`, {
//            method: 'DELETE'
//        })
//        .then(() => {
//            alert('삭제가 완료되었습니다.');
//            location.replace('/articles');
//        });
        function success() {
            alert('삭제가 완료되었습니다.');
            location.replace('/articles');
        }

        function fail() {
            alert('삭제가 실패했습니다.');
            location.replace('/articles');
        }

        httpRequest("DELETE", "/api/articles/" + id, null, success, fail);
    });
}

// id가 modify-btn 인 엘리먼트 조회
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');
        body = JSON.stringify({
                    title: document.getElementById('title').value,
                    content: document.getElementById('content').value
                });

//        fetch(`/api/articles/${id}`, {
//            method: 'PUT',
//            headers: {
//                "Content-Type": "application/json",
//            },
//            body: JSON.stringify({
//                title: document.getElementById('title').value,
//                content: document.getElementById('content').value
//            })
//        })
//        .then(() => {
//            alert('수정이 완료되었습니다.');
//            location.replace(`/articles/${id}`);
//        })
        function success() {
            alert('수정이 완료되었습니다.');
            location.replace('/articles');
        }

        function fail() {
            alert('수정이 실패했습니다.');
            location.replace('/articles');
        }

        httpRequest("PUT", "/api/articles/" + id, body, success, fail);
    });
}

// 생성 기능
const createButton = document.getElementById('create-btn');

//if (createButton) {
//    // 등록 버튼을 클릭하면 /api/articles로 요청을 보냄
//    createButton.addEventListener('click', event => {
//
//        fetch('/api/articles', {
//            method: "POST",
//            headers: {
//                "Content-Type": "application/json",
//            },
//            body: JSON.stringify({
//                title: document.getElementById('title').value,
//                content: document.getElementById('content').value
//            }),
//        })
//        .then(() => {
//            alert('등록 완료되었습니다.');
//            location.replace('/articles');
//        })
//    });
//}

if (createButton) {
    // 등록 버튼을 클릭하면 /api/articles로 요청을 보냄
    createButton.addEventListener('click', event => {
        body = JSON.stringify({
            title: document.getElementById("title").value,
            content: document.getElementById("content").value,
        });

        function success() {
            alert('등록 완료되었습니다.');
            location.replace('/articles');
        }

        function fail() {
            alert('등록 실패했습니다.');
            location.replace('/articles');
        }

        httpRequest("POST", "/api/articles", body, success, fail);
    });
}

// 쿠키를 가져오는 함수
function getCookie(key) {

    var result = null;
    var cookie = document.cookie.split(";"); // 브라우져의 cookie에 접근

    // .some() 함수는 비교 논리를 적용하여 배열의 모든 요소를 반복합니다.
    // 비교 중에 참을 반환하는 첫 번째 항목을 찾으면 즉시 반복을 중단합니다.
    // 로직을 만족하는 요소가 하나라도 있으면 반환값은 true 이 됩니다. (or 조건)
    // object 의 ID를 비교하는 데 사용되는 기준입니다.
    // 프로퍼티는 숫자를 저장하므로 == 또는 === 로 비교하면 참조(reference) 가 아닌 값(value) 을 사용합니다.
    cookie.some(function (item) {
        item = item.replace(" ", "");

        var dic = item.split("=");
        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}

// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            // 로컬 스토리지에서 엑세스 토큰 값을 가져와 헤더에 추가 - 123
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        },
        body: body,
    })
    .then((response) => {
        if(response.status === 200 || response.status === 201){
            return success();
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
            .then((res) => {
                if (res.ok) {
                    return res.json();
                }
            })
            .then((result) => {
                // 재발급이 성공하면 로컬 스토리지값을 새로운 엑세스 토큰으로 교체
                localStorage.setItem('access_token', result.accessToken);
                httpRequest(method, url, body, success, fail);
            })
            .catch((error) => fail());
        } else {
            return fail();
        }
    });

}