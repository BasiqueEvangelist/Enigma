package cuchaz.enigma;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableListMultimap;

import cuchaz.enigma.api.service.EnigmaService;
import cuchaz.enigma.api.service.EnigmaServiceType;

public final class EnigmaServices {
	private final ImmutableListMultimap<EnigmaServiceType<?>, RegisteredService<?>> services;

	EnigmaServices(ImmutableListMultimap<EnigmaServiceType<?>, RegisteredService<?>> services) {
		this.services = services;
	}

	public <T extends EnigmaService> Collection<T> get(EnigmaServiceType<T> type) {
		return Collections2.transform(getWithIds(type), RegisteredService::service);
	}

	@SuppressWarnings("unchecked")
	public <T extends EnigmaService> List<RegisteredService<T>> getWithIds(EnigmaServiceType<T> type) {
		return (List<RegisteredService<T>>)(Object) services.get(type);
	}

	public record RegisteredService<T extends EnigmaService>(String id, T service) {

	}
}
