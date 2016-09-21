require 'sinatra'
require 'rest-client'
require 'sequel'
require 'json'
# Create a SQLite3 database

DB = Sequel.connect('sqlite://gcm-test.db')

# Create a Device table if it doesn't exist
DB.create_table? :Device do
  primary_key :reg_id
  String :user_id
  String :reg_token
  String :os, :default => 'android'
end

Device = DB[:Device]  # create the dataset

# Registration endpoint mapping reg_token to user_id
# POST /register?reg_token=abc&user_id=123
post '/register' do
  if Device.filter(:reg_token => params[:reg_token]).count == 0
    device = Device.insert(:reg_token => params[:reg_token], :user_id => params[:user_id], :os => 'android')
  end
end

# Ennpoint for sending a message to a user
# POST /send?user_id=123&title=hello&body=message
post '/send' do
  # Find devices with the corresponding reg_tokens
  reg_tokens = Device.filter(:user_id => params[:user_id]).map(:reg_token).to_a
  if reg_tokens.count != 0
    send_gcm_message(params[:title], params[:body], reg_tokens)
  end
end

# Sending logic
# send_gcm_message(["abc", "cdf"])
def send_gcm_message(title, body, reg_tokens)
  # Construct JSON payload
  post_args = {
    # :to field can also be used if there is only 1 reg token to send
    :registration_ids => reg_tokens,
    :data => {
      :title  => title,
      :body => body,
      :anything => "foobar"
    }
  }

  # Send the request with JSON args and headers
  RestClient.post 'https://gcm-http.googleapis.com/gcm/send', post_args.to_json,
    :Authorization => 'key=AIzaSyC0EVhbTNqgr69Pr8pWEdPDe5SSSIWhaU0', :content_type => :json, :accept => :json
end
