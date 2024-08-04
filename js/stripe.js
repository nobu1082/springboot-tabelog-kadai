 const stripe = stripe('sk_test_51OLEXaJsSQOmZTMCrcb62esT4RG6bjjugAXnoxNd6E7gCTCJPdCGPRK1KgYHvgoro0A3ckUdXMCOoMeZmp6uwDeX00agPzG7vY');
 const paymentButton = document.querySelector('#paymentButton');
 
 paymentButton.addEventListener('click', () => {
   stripe.redirectToCheckout({
     sessionId: sessionId
   })
 });