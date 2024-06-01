package com.example.nobukuni2023.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nobukuni2023.entity.Favorite;
import com.example.nobukuni2023.entity.Store;
import com.example.nobukuni2023.entity.User;
import com.example.nobukuni2023.repository.FavoriteRepository;
import com.example.nobukuni2023.repository.UserRepository;


@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final UserRepository userRepository;
	
	public FavoriteService(FavoriteRepository favoriteRepository,UserRepository userRepository) {
		this.favoriteRepository = favoriteRepository;
		this.userRepository = userRepository;
	}
	
	@Transactional
	public boolean subscribe(Store store , User user) {
		Favorite existingFavorite =favoriteRepository.findByStoreAndUser (store ,user);
		if( existingFavorite == null) {
		
		Favorite favorite = new Favorite();
		favorite.setStore(store);
		favorite.setUser(user);
		favoriteRepository.save(favorite);
		return true;
	}
		return false;
	}
	@Transactional
	public void unsubscribe(Store store, User user) {
	    Favorite favorite = favoriteRepository.findByStoreAndUser(store, user);
	    if (favorite != null) {
	        favoriteRepository.delete(favorite);
	    }
	}
	
}

