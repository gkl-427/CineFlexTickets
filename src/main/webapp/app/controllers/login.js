import Ember from 'ember';
import ENV from '../config/environment';

export default Ember.Controller.extend({

    currentUser: Ember.inject.service(),
    actions: {
        sendRecord: function () {
            let mobilenumber = this.get('mobilenumber');
            let password = this.get('password');
            const user = {
                mobilenumber: mobilenumber,
                password: password,
            };
            Ember.$.ajax({
                url: ENV.APP.API_URL + '/login',
                type: 'POST',
                data: user,
                dataType: 'json'
            }).then(response => {
                if (response.success) {
                    localStorage.setItem('userData', JSON.stringify(response));
                    this.get('currentUser').setUserData(response);
                    this.transitionToRoute('dashboard.user');
                }
                else {
                    this.set('errorMessage', 'Invalid Credentials');
                    this.setProperties({
                        mobilenumber: '',
                        password: ''
                    });

                }
            }).catch(() => {
                let $element = Ember.$('.element-to-animate');
                $element.addClass('fade-out');
          
                Ember.run.later(() => {
                  this.transitionTo('dashboard.listalltheatres');
                }, 500); 
            });
        }
    }
});


