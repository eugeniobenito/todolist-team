function aceptInvitacion(urlPatch) {
    console.log("hey");
    const url = 'http://localhost:8080' + urlPatch;
    console.log(url);
    fetch(url, {
        method: 'PATCH'
    }).then((res) => {
        location.reload();
    })
}

function deleteInvitacion(urlBorrar) {
    const url = 'http://localhost:8080' + urlBorrar;
    fetch(url, {
        method: 'DELETE'
    }).then((res) => {
        location.reload();
    })
}