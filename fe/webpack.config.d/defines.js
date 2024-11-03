const stompBrokerUrl = ({
    development: "ws://localhost:9080/stomp",
    production: "wss://flovs.link/stomp",
})[config.mode];

const webpack = require("webpack");
config.plugins.push(new webpack.DefinePlugin(
    {
        STOMP_BROKER_URL: JSON.stringify(stompBrokerUrl),
    }
));
