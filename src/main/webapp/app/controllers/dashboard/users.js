import Ember from 'ember';
import ENV from '../../config/environment';

export default Ember.Controller.extend({
    currentUser: Ember.inject.service(),
    selectedUser: null,
    isUserAvailable: true,
    filteredData: null,

    isAdmin: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') == 3;
    }),
    isManager: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') == 2;
    }),
    isUser: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') == 1;
    }),
    
    actions: {
        onpromote(params) {
            let userid = params.userid;
            let formData = {
                role: 2
            };
            Ember.$.ajax({
                url: ENV.APP.API_URL + '/users/' + userid,
                type: 'PUT',
                data: JSON.stringify(formData),
                dataType: 'JSON',
                withCredentials: true

            }).then(response => {
                if (response.success) {
                    Ember.set(params, 'role', 2);
                } else {
                    alert('Something Went Wrong')
                }
            }).catch(() => {
                let $element = Ember.$('.element-to-animate');
                $element.addClass('fade-out');

                Ember.run.later(() => {
                    this.transitionTo('dashboard');
                }, 500);
            });

        },
        searchCustomers() {
            let searchQuery = this.get("searchQuery");
            let filteredData = this.get("model").filter((model) => {
              return model.firstname.toLowerCase().includes(searchQuery.toLowerCase());
            });
            this.set("filteredData", filteredData);
            if (filteredData == null && searchQuery != null) {
              this.set("searchUserTyped", false);
              this.set("isUserAvailable", true);
            } else if (filteredData != null) {
              this.set("isUserAvailable", false);
              this.set("searchUserTyped", true);
            } else if (searchQuery == null) {
              this.set("searchUserTyped", false);
            }
        },

        demote(params) {
            let userid = params.userid;
            let formData = {
                role: 1
            };
            Ember.$.ajax({
                url: ENV.APP.API_URL + '/users/' + userid,
                type: 'PUT',
                data: JSON.stringify(formData),
                dataType: 'JSON',
                withCredentials: true

            }).then(response => {
                if (response.success) {
                    Ember.set(params, 'role', 1);
                } else {
                    alert('Something Went Wrong')
                }
            }).catch(() => {
                let $element = Ember.$('.element-to-animate');
                $element.addClass('fade-out');

                Ember.run.later(() => {
                    this.transitionTo('dashboard');
                }, 500);
            });
        }
    },
    
    logout() {
          this.get('currentUser').logout();
          this.transitionToRoute('application');
    }
});
