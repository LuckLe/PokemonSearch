package com.example.rocketreserver.apollo

import com.apollographql.apollo3.ApolloClient

val apolloClient = ApolloClient.Builder()
    .serverUrl("https://beta.pokeapi.co/graphql/v1beta")
    .build()