import Ember from 'ember';

export default Ember.Controller.extend({
    tickeid:null,
    actions: {
        onclick(params){
        let ticketId=params.ticketid;      
        let formData = {
            tickeid:ticketId,
        };
        Ember.$.ajax({
            url: ENV.APP.API_URL + '/bookings/'+ticketId + '?cancel',
            type: 'PUT',
            data: JSON.stringify(formData),
            dataType: 'JSON',            
        }).then(response => {
            if(response.success){
                Ember.set(params,'showstatus',!showStatus);
            }else{
                alert('Something Went Wrong');
              }
            }).catch(() => {
                
                this.transitionToRoute('signup');
            });
        }
    }
});
