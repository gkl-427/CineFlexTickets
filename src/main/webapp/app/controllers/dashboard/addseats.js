import Ember from 'ember';
import ENV from '../../config/environment';

export default Ember.Controller.extend({

    currentUser: Ember.inject.service(),
    isAdmin: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') == 3;
    }),
    actions:
    {
        sendRecord() {
            let formData = {
                tieroneprice:this.get("tieroneprice"),
                tiertwoprice:this.get("tiertwoprice"),
                tierthreeprice:this.get("tierthreeprice")
            };
            Ember.$.ajax({
                url: ENV.APP.API_URL + '/theatres?addseats',
                type: 'POST',
                data: JSON.stringify(formData),
                dataType: 'application/json'
            }).then(response => {
                if (response.success) {
                    let $element = Ember.$('.element-to-animate');
                    $element.addClass('fade-out');

                    Ember.run.later(() => {
                        this.transitionToRoute('dashboard.listalltheatres');
                    }, 500);
                } else {
                    alert('Something Went Wrong');
                }
            }).catch(() => {
                let $element = Ember.$('.element-to-animate');
                $element.addClass('fade-out');

                Ember.run.later(() => {
                    this.transitionToRoute('dashboard.listalltheatres');
                }, 500);

            });
        }
    }
});
