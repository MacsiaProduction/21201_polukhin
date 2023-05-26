package ru.nsu.fit.m_polukhin;

import ru.nsu.fit.m_polukhin.exceptions.FileMissingException;
import ru.nsu.fit.m_polukhin.exceptions.PathFactoryException;
import ru.nsu.fit.m_polukhin.modules.DuCompoundFileType;
import ru.nsu.fit.m_polukhin.modules.DuFileType;
import ru.nsu.fit.m_polukhin.modules.MetaType;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TreeFactory {
    private final List<MetaType<? extends DuFileType>> metaTypes;

    public TreeFactory(List<Class<? extends MetaType<? extends DuFileType>>> classList) throws PathFactoryException {
        this.metaTypes = new ArrayList<>();
        for (Class<? extends MetaType<? extends DuFileType>> clazz : classList) {
            try {
                this.metaTypes.add(clazz.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new PathFactoryException("Exception while instantiation of metaTypes");
            }
        }
    }

    private DuFileType create(Path path, JduOptions jduOptions) throws PathFactoryException {
        var metaType = getMetaOf(path);
        if (metaType == null) throw new PathFactoryException("Path" + path + "can't be recognized as any type");
        return metaType.createFileType(path, jduOptions);
    }

    public DuFileType buildTree(Path root, JduOptions options) throws PathFactoryException, FileMissingException {
        List<List<DuFileType>> layers = new ArrayList<>();
        var initialFile = create(root, options);
        layers.add(List.of(initialFile));
        for (int i = 1; i < options.depth() && layers.get(i-1).size() != 0; i++) {
           layers.add(initNextLayer(layers.get(i-1), options));
        }
        for (int i = layers.size() - 1; i >= 0; i--) {
            calculateLayer(layers.get(i));
        }
        return initialFile;
    }

    private List<DuFileType> initNextLayer(List<DuFileType> layer, JduOptions options) throws FileMissingException, PathFactoryException {
        List<DuFileType> nextLayer = new ArrayList<>();
        for (var element: layer) {
            if(element instanceof DuCompoundFileType) {
                var childrenAsPaths = ((DuCompoundFileType) element).getChildrenAsPaths();
                List<DuFileType> childrenAsTypes = new ArrayList<>();
                for (var path : childrenAsPaths) {
                    var duFile = create(path, options);
                    nextLayer.add(duFile);
                    childrenAsTypes.add(duFile);
                }
                ((DuCompoundFileType) element).setChildren(childrenAsTypes);
            }
        }
        return nextLayer;
    }

    private void calculateLayer(List<DuFileType> layer) throws FileMissingException {
        for(var element : layer) {
            var meta = getMetaOf(element.path());
            assert meta != null;
            meta.calculateSize(element);
        }
    }

    private MetaType<? extends DuFileType> getMetaOf(Path path) {
        for (var metaType : metaTypes) {
            if (metaType.isCompatible(path)) {
                return metaType;
            }
        }
        return null;
    }
}