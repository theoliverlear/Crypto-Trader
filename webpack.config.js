const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: [
        './src/main/resources/static/script/accountScript.ts',
        './src/main/resources/static/script/getStartedScript.ts',
        './src/main/resources/static/script/globalScript.ts',
        './src/main/resources/static/script/homeScript.ts',
        './src/main/resources/static/script/PortfolioAsset.ts',
        './src/main/resources/static/script/portfolioScript.ts',
        './src/main/resources/static/script/traderScript.ts',
        './src/main/resources/static/script/userScript.ts',
        './src/main/resources/static/script/PortfolioView.ts',
        './src/main/resources/static/script/graphScript.ts',
        './src/main/resources/static/script/PortfolioHistories.ts',
        './src/main/resources/static/script/BuyTrade.ts',
        './src/main/resources/static/script/SellTrade.ts',
        './src/main/resources/static/script/Trade.ts',
        './src/main/resources/static/script/TradeType.ts',
        './src/main/resources/static/script/imageUploadScript.ts'
    ],
    module: {
        rules: [
            {
                test: /\.ts$/,
                exclude: /node_modules/,
                use: [
                    {
                        loader: 'babel-loader',
                        options: {
                            presets: [
                                '@babel/preset-env',
                                '@babel/preset-typescript'
                            ]
                        }
                    },
                    'ts-loader'
                ]
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader'],
            },
        ],
    },
    resolve: {
        extensions: [
            '.ts',
            '.js'
        ],
    },
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, './src/main/resources/static/script'),
    },
    mode: 'development',
    plugins: [
        new webpack.ProvidePlugin({
            $: 'jquery',
            jQuery: 'jquery'
        })
    ]
};