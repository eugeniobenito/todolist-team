function deleteInvitation(urlBorrar) {
    const url = 'http://localhost:8080' + urlBorrar;
    fetch(url, {
        method: 'DELETE'
    }).then((res) => {
        location.reload();
    })

}