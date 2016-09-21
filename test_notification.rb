require 'gcm'
gcm = GCM.new("AIzaSyDu3W-FnpvKH_U7CG3wYoQTvY_Su67t0s4")
reg_tokens = ["fP6-4ytom4I:APA91bF7G1yCOSCTYZKlDwn1qg3FncfL_ixYxBcF7L-BthpnCUQiufDhaYsdVG11bMZscdFcWOYlI4knRqLlK26UkM3CkJEBjXD3lSGxzOt68gyV0XZes-ZJNuWO8-ihfnHot5GEmD9s"]
options = { :notification => { :title =>"foobar", :body => "this is a longer message" } }
response = gcm.send(reg_tokens, options)
