const sideMenu=document.querySelector("aside");
const menuBtn=document.querySelector("#menu-btn");
const closeBtn=document.querySelector("#close-btn");
const themeToggler=document.querySelector(".theme-toggler");
//show side bar
menuBtn.addEventListener('click',()=>{
    sideMenu.style.display='block';
})
//close side bar
closeBtn.addEventListener('click',()=>{
    sideMenu.style.display='none';
})
//change theme
themeToggler.addEventListener('click', ()=>{
    document.body.classList.toggle('dark-theme-variables');

    themeToggler.querySelector('span:nth-child(1)').classList.toggle('active');
    themeToggler.querySelector('span:nth-child(2)').classList.toggle('active');

})

//fill pets in table
Booking.forEach(book=>{
    const tr=document.createElement("tr");
    const trContent=  `  
    <td>${book.petName}</td>
    <td>${book.petAge}</td>
    <td>${book.bookedBy}</td>
    <td>${book.status}</td>`;
    tr.innerHTML=trContent;
    document.querySelector('table tbody').appendChild(tr);
})

