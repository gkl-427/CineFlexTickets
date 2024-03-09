import Ember from 'ember';
import ENV from '../../config/environment';

export default Ember.Controller.extend({
    currentUser: Ember.inject.service(),

    isManager: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') == 2;
    }),
    actions: {
        onclick(params){
        let showid=params.showid;
        let showStatus = params.showstatus;
        let formData = {
            showstatus:!showStatus
        };
        Ember.$.ajax({
            url: ENV.APP.API_URL + '/shows/'+showid,
            type: 'PUT',
            data: JSON.stringify(formData),
            dataType: 'JSON',
            withCredentials: true
            
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
